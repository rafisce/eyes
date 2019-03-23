package com.eyes.eyes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//c3 3
public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private FirebaseAuth mAuth;

    private Button loginBtn;
    private EditText loginmail;
    private EditText loginPassword;

    private ProgressDialog loadingBar;
    // this method overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("התחברות");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();


        loginmail = (EditText) findViewById(R.id.login_email);
        loginPassword =(EditText) findViewById(R.id.login_password);

        loginBtn = (Button) findViewById(R.id.login_button);

        loadingBar = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User("",loginmail.getText().toString(),loginPassword.getText().toString());
                loginUserAccount(user);
            }
        });
    }

    private void loginUserAccount(User user)
    {
        if(TextUtils.isEmpty(user.getEmail()))
        {
            Toast.makeText(LoginActivity.this,"אנא הכנס אימיל",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(user.getPassword()))
        {
            Toast.makeText(LoginActivity.this,"אנא הכנס סיסמא",Toast.LENGTH_SHORT).show();
        }
        else
        {
           loadingBar.setTitle("מתחבר לחשבון");
           loadingBar.setMessage("אנא המתן בזמן שאנחנו מאמתים את חשבונך");
           loadingBar.show();

           mAuth.signInWithEmailAndPassword(user.getEmail(),user.getPassword())
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful())
                           {
                               Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                               mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           }
                           else
                           {
                               Toast.makeText(LoginActivity.this,"שם משתמש או סיסמא לא נכונים, אנא בדוק את שם משתמש והסיסמא",Toast.LENGTH_SHORT).show();
                           }
                           loadingBar.dismiss();
                       }
                   });
        }
    }
}
