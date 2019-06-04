package com.eyes.eyes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;


public class RegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private ProgressDialog loadingBar;
    private DesEncryption des = new DesEncryption();
    private static final String KEY_ = "EYE#KEY1";
    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDataReference;

    private EditText RegisterUserName;
    private EditText RegisterUserEmail;
    private EditText RegisterUserPassword;
    private Button createAccountBtn;
    private Button createHebAccountBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("הרשמה");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RegisterUserName = (EditText) findViewById(R.id.register_name);
        RegisterUserEmail = (EditText) findViewById(R.id.register_email);
        RegisterUserPassword = (EditText) findViewById(R.id.register_password);
        createAccountBtn = (Button) findViewById(R.id.creat_account_button);
        createHebAccountBtn = (Button) findViewById(R.id.creat_heb_account_button);
        loadingBar = new ProgressDialog(this);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common user = new Common(RegisterUserName.getText().toString(), RegisterUserEmail.getText().toString(), RegisterUserPassword.getText().toString(), "eng");
                RegisterAccount(user);
            }
        });

        createHebAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common user = new Common(RegisterUserName.getText().toString(), RegisterUserEmail.getText().toString(), RegisterUserPassword.getText().toString(), "heb");
                RegisterAccount(user);
            }
        });

    }


    private void RegisterAccount(final Common user) {
        Log.d("RegisterAccount", user.getName());
        if (TextUtils.isEmpty(user.getName())) {
            Toast.makeText(RegisterActivity.this, "אנא רשום את שמך", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(user.getEmail())) {
            Toast.makeText(RegisterActivity.this, "אנא רשום את האימייל שלך", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            Toast.makeText(RegisterActivity.this, "אנא רשום את הסיסמא שלך", Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("יוצר חשבון חדש");
            loadingBar.setMessage("אנא המתן בזמן שאנחנו יוצרים חשבון עבורך");
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                String name = des.Encrypt(user.getName(),KEY_);
                                String email = des.Encrypt(user.getEmail(),KEY_);
                                String type = des.Encrypt(user.getTYPE(),KEY_);
                                String pass = des.Encrypt(user.getPassword(),KEY_);
                                storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
                                storeUserDefaultDataReference.child("user_name").setValue(name);
                                storeUserDefaultDataReference.child("language").setValue(user.getLanguage());
                                storeUserDefaultDataReference.child("user_email").setValue(email);
                                storeUserDefaultDataReference.child("user_type").setValue(type);
                                storeUserDefaultDataReference.child("records").setValue(0);
                                storeUserDefaultDataReference.child("dest_counter").setValue(0);
                                storeUserDefaultDataReference.child("online").setValue("false");
                                storeUserDefaultDataReference.child("active").setValue("true");
                                storeUserDefaultDataReference.child("user_password").setValue(pass);

                                Calendar calendar = Calendar.getInstance();
                                String created_date = DateFormat.getDateInstance().format(calendar.getTime());
                                storeUserDefaultDataReference.child("create_date").setValue(created_date)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())

                                                {
                                                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                    finish();
                                                }

                                            }
                                        });


                            } else {
                                Toast.makeText(RegisterActivity.this, "קרתה טעותת נסה שוב", Toast.LENGTH_SHORT);
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
