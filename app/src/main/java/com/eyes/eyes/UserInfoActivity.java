package com.eyes.eyes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference user;
    private TextView user_name;
    private  TextView user_email;
    private  TextView lang;
    private TextView created_date;
    private Toolbar mToolbar;
    private Button dest;
    private static final String KEY_ = "EYE#KEY1";
    private DesEncryption des = new DesEncryption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mToolbar = (Toolbar) findViewById(R.id.doc_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("חזור");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dest = (Button) findViewById(R.id.use_dest);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        user_name = (TextView) findViewById(R.id.user_name_text_view);
        user_email = (TextView) findViewById(R.id.user_email_text_view);
        lang = (TextView) findViewById(R.id.lang_text_view);
        created_date = (TextView) findViewById(R.id.created_date_text_view);
        user.child("online").setValue("true");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user_name.setText("user: "+des.Decrypt(dataSnapshot.child("user_name").getValue().toString(),KEY_));
                user_email.setText("email: "+des.Decrypt(dataSnapshot.child("user_email").getValue().toString(),KEY_));
                lang.setText("language: "+dataSnapshot.child("language").getValue().toString());
                created_date.setText("joined in: "+dataSnapshot.child("create_date").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, DestinationsActivity.class);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            Intent startPageIntent = new Intent(UserInfoActivity.this,StartPageActivity.class);
            startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startPageIntent);
            finish();
        }else{
            DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            current_user.child("online").setValue("true");
        }
    }

    @Override
    protected void onPause() {
        DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        current_user.child("online").setValue("false");
        super.onPause();
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
