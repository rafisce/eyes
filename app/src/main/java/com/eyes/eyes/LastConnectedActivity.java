package com.eyes.eyes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class LastConnectedActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Toolbar mToolbar;

    private ArrayList<LastConected> useList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_connected);


        mToolbar = (Toolbar) findViewById(R.id.last_connected_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("מנהל");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectUsers((Map<String,Object>) dataSnapshot.getValue());

                        mRecyclerView = findViewById(R.id.recyclerView);
                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(LastConnectedActivity.this);
                        mAdapter = new LastConnectedAdapter(useList);

                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private void collectUsers(Map<String,Object> users) {


        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            useList.add(new LastConected((String)singleUser.get("user_name"),(String)singleUser.get("last_connected")));
                Collections.sort(useList, new Comparator<LastConected>() {
                public int compare(LastConected m1, LastConected m2) {
                    return m1.getTime().compareTo(m2.getTime());
                }
            });
                Collections.reverse(useList);

        }

    }

}
