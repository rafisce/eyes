package com.eyes.eyes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WelcomActivity extends AppCompatActivity {

    private DatabaseReference current_user=null;
    private FirebaseAuth mAuth;
    private static final String KEY_ = "EYE#KEY1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                    mAuth = FirebaseAuth.getInstance();


                    try {
                        current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(current_user!=null) {
                        current_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                DesEncryption des = new DesEncryption();
                                if (des.Decrypt(dataSnapshot.child("user_type").getValue().toString(), KEY_).equals("admin")) {
                                    Intent mainIntent = new Intent(WelcomActivity.this, AdminActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                } else if (des.Decrypt(dataSnapshot.child("user_type").getValue().toString(), KEY_).equals("worker") && !getIntent().hasExtra("from")) {
                                    Intent mainIntent = new Intent(WelcomActivity.this, WorkerActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                } else {
                                    Intent mainIntent = new Intent(WelcomActivity.this, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else {

                        Intent mainIntent = new Intent(WelcomActivity.this, StartPageActivity.class);
                        startActivity(mainIntent);
                    }
                }

            }
        };
        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
