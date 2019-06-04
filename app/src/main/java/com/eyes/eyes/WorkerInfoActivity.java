package com.eyes.eyes;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkerInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference user;
    private TextView user_name;
    private TextView user_email;
    private TextView lang;
    private TextView created_date;
    private Toolbar mToolbar;
    private static final String KEY_ = "EYE#KEY1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_info);

        mToolbar = (Toolbar) findViewById(R.id.work_doc_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("חזרה");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        user = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        user_name = (TextView) findViewById(R.id.user_name_text_view);
        user_email = (TextView) findViewById(R.id.user_email_text_view);
        lang = (TextView) findViewById(R.id.lang_text_view);
        created_date = (TextView) findViewById(R.id.created_date_text_view);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DesEncryption des = new DesEncryption();

                user_name.setText("user: " + des.Decrypt(dataSnapshot.child("user_name").getValue().toString(),KEY_));
                user_email.setText("email: " + des.Decrypt(dataSnapshot.child("user_email").getValue().toString(),KEY_));
                lang.setText("language: " + dataSnapshot.child("language").getValue().toString());
                created_date.setText("joined in: " + dataSnapshot.child("create_date").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
