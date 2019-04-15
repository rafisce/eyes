package com.eyes.eyes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class DestinationsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;

    private Toolbar mToolbar;

    private ArrayList<Destination> destList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.destinations_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("חזרה");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String current = mAuth.getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(current).child("dest_list");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectDestinations((ArrayList<String>) dataSnapshot.getValue());

                        mRecyclerView = findViewById(R.id.recyclerView1);
                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(DestinationsActivity.this);
                        mAdapter = new DestinationsAdapter(destList);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private void collectDestinations(ArrayList<String> destinations) {

         int count = destinations.size();
        //iterate through each user, ignoring their UID
        for (String entry : destinations){

            //Get user map
            String dest = entry;
            //Get phone field and append to list
            destList.add(new Destination(dest,String.valueOf(count-1)));
            count--;
            Collections.sort(destList, new Comparator<Destination>() {
                public int compare(Destination m1, Destination m2) {
                    return m1.getNum().compareTo(m2.getNum());
                }
            });


        }

    }
}
