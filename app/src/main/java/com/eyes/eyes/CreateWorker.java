package com.eyes.eyes;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Map;

public class CreateWorker extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog loadingBar;
    private DatabaseReference storeUserDefaultDataReference;
    private EditText WorkerUserName;
    private EditText WorkerUserEmail;
    private EditText WorkerUserPassword;
    private Button createAccountBtn;
    private FirebaseAuth mAuth1;
    private FirebaseAuth mAuth2;
    private static final String KEY_ = "EYE#KEY1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_worker);


        mToolbar = (Toolbar) findViewById(R.id.create_worker_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("ניהול");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WorkerUserName = (EditText) findViewById(R.id.worker_name);
        WorkerUserEmail = (EditText) findViewById(R.id.worker_email);
        WorkerUserPassword = (EditText) findViewById(R.id.worker_password);
        createAccountBtn = (Button) findViewById(R.id.create_worker);
        loadingBar = new ProgressDialog(this);
        mAuth1 = FirebaseAuth.getInstance();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("[https://eyes-45129.firebaseio.com]")
                .setApiKey("AIzaSyBQ0Q_n562d4XdWo1vgH0TF270I9EwSjoE")
                .setApplicationId("eyes-45129").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
        }



        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Worker user = new Worker(WorkerUserName.getText().toString(), WorkerUserEmail.getText().toString(), WorkerUserPassword.getText().toString(), "eng");
                RegisterAccount(user);
            }
        });



    }


    private void RegisterAccount(final Worker user) {
        Log.d("RegisterAccount", user.getName());
        if (TextUtils.isEmpty(user.getName())) {
            Toast.makeText(CreateWorker.this, "אנא רשום את שמך", Toast.LENGTH_LONG).show();
            //
        }
        if (TextUtils.isEmpty(user.getEmail())) {
            Toast.makeText(CreateWorker.this, "אנא רשום את האימייל שלך", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            Toast.makeText(CreateWorker.this, "אנא רשום את הסיסמא שלך", Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("יוצר חשבון חדש");
            loadingBar.setMessage("אנא המתן בזמן שאנחנו יוצרים חשבון עבורך");
            loadingBar.show();


            mAuth2.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String currentUserId = mAuth2.getCurrentUser().getUid();
                                DesEncryption des = new DesEncryption();
                                storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
                                storeUserDefaultDataReference.child("user_name").setValue(des.Encrypt(user.getName(),KEY_));
                                storeUserDefaultDataReference.child("language").setValue(user.getLanguage());
                                storeUserDefaultDataReference.child("user_email").setValue(des.Encrypt(user.getEmail(),KEY_));
                                storeUserDefaultDataReference.child("user_type").setValue(des.Encrypt(user.getTYPE(),KEY_));
                                storeUserDefaultDataReference.child("records").setValue(0);
                                storeUserDefaultDataReference.child("dest_counter").setValue(0);
                                storeUserDefaultDataReference.child("active").setValue("true");
                                storeUserDefaultDataReference.child("last_connected").setValue("עדיין לא התחבר");

                                Calendar calendar = Calendar.getInstance();
                                String created_date = DateFormat.getDateInstance().format(calendar.getTime());
                                storeUserDefaultDataReference.child("create_date").setValue(created_date)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())

                                                {
                                                    mAuth2.signOut();
                                                    Toast.makeText(CreateWorker.this, "משתמש נוצר בהצלחה", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });


                            } else {
                                Toast.makeText(CreateWorker.this, "קרתה טעותת נסה שוב", Toast.LENGTH_SHORT);
                            }

                            loadingBar.dismiss();
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: // Intercept the click on the home button
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


