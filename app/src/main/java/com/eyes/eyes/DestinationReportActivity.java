package com.eyes.eyes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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

public class DestinationReportActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseAuth mAuth;

    private Toolbar mToolbar;

    private ArrayList<Destination> destinationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_report);

        mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.destination_Report_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("חזרה");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String current = mAuth.getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        destinationList = collectDestinations((Map<String, Object>) dataSnapshot.getValue());

                        mRecyclerView = findViewById(R.id.recyclerView1);
                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(DestinationReportActivity.this);
                        mAdapter = new DestinationsAdapter(destinationList);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    public ArrayList<Destination> collectDestinations(Map<String, Object> users) {
        ArrayList<Destination> destList = new ArrayList<>();
        ArrayList<ArrayList<String>> desList = new ArrayList<>();
        Map singleUser;
        int count=0;
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            singleUser = (Map) entry.getValue();
            Object value = singleUser.get("dest_list");
            if(value!=null && value instanceof ArrayList){
                count+= ((ArrayList) value).size();
                desList.add((ArrayList<String>) value);
            }
        }

        for (ArrayList<String> arr: desList){
            for (String str:arr) {
                String dest = str;
                //Get phone field and append to list
                destList.add(new Destination(dest, String.valueOf(count)));
                count--;
                Collections.sort(destList, new Comparator<Destination>() {
                    public int compare(Destination m1, Destination m2) {
                        return m1.getNum().compareTo(m2.getNum());
                    }
                });
            }
        }
        return destList;
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
