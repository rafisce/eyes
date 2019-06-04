package com.eyes.eyes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference current_user;
    private Button loginBtn;
    private EditText loginMail;
    private EditText loginPassword;
    private ProgressDialog loadingBar;
    private static final String KEY_ = "EYE#KEY1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("התחברות");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();


        loginMail = (EditText) findViewById(R.id.login_email);
        loginPassword = (EditText) findViewById(R.id.login_password);

        loginBtn = (Button) findViewById(R.id.login_button);

        loadingBar = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User("", loginMail.getText().toString(), loginPassword.getText().toString());
                loginUserAccount(user);
            }
        });
    }

    private void loginUserAccount(final User user) {


        if (TextUtils.isEmpty(user.getEmail())) {
            Toast.makeText(LoginActivity.this, "אנא הכנס אימיל", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            Toast.makeText(LoginActivity.this, "אנא הכנס סיסמא", Toast.LENGTH_SHORT).show();
        } else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    for (Map.Entry<String, Object> entry : map.entrySet()) {

                        Map singleUser = (Map) entry.getValue();
                        try {
                            if (desCheck(singleUser.get("user_email").toString(), user.getEmail()) && desCheck(singleUser.get("user_password").toString(), user.getPassword())) {
                                if (checkActive(singleUser.get("active").toString())) {

                                    loadingBar.setTitle("מתחבר לחשבון");
                                    loadingBar.setMessage("אנא המתן בזמן שאנחנו מאמתים את חשבונך");
                                    loadingBar.show();

                                    mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                                                        current_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (checkCommon(dataSnapshot.child("user_type").getValue().toString())) {
                                                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(mainIntent);
                                                                    finish();
                                                                } else if (checkAdmin(dataSnapshot.child("user_type").getValue().toString())) {
                                                                    Intent mainIntent = new Intent(LoginActivity.this, AdminActivity.class);
                                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(mainIntent);
                                                                    finish();
                                                                } else if (checkWorker(dataSnapshot.child("user_type").getValue().toString())) {
                                                                    Intent mainIntent = new Intent(LoginActivity.this, WorkerActivity.class);
                                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(mainIntent);
                                                                    finish();
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });


                                                    } else {
                                                        Toast.makeText(LoginActivity.this, "שם משתמש או סיסמא לא נכונים, אנא בדוק את שם משתמש והסיסמא", Toast.LENGTH_SHORT).show();
                                                    }
                                                    loadingBar.dismiss();
                                                }
                                            });

                                } else if (singleUser.get("user_email").toString().equals(user.getEmail()) && checkNotActive(singleUser.get("active").toString())) {
                                    Toast.makeText(LoginActivity.this, "חשבונך מושהה", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }

    public boolean checkAdmin(String str) {
        DesEncryption des = new DesEncryption();
        String res = des.Decrypt(str, KEY_);
        if (res.equals("admin"))
            return true;
        return false;
    }

    public boolean checkCommon(String str) {
        DesEncryption des = new DesEncryption();
        String res = des.Decrypt(str, KEY_);
        if (res.equals("common"))
            return true;
        return false;
    }

    public boolean checkWorker(String str) {
        DesEncryption des = new DesEncryption();
        String res = des.Decrypt(str, KEY_);
        if (res.equals("worker"))
            return true;
        return false;
    }

    public boolean checkActive(String str) {
        return str.equals("true");
    }

    public boolean checkNotActive(String str) {
        return str.equals("false");
    }

    public boolean desCheck(String str, String str2) {
        DesEncryption des = new DesEncryption();
        String res = des.Decrypt(str, KEY_);
        if (res.equals(str2))
            return true;
        return false;
    }


}
