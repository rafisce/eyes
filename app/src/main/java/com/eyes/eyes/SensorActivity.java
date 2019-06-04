package com.eyes.eyes;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class SensorActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private ArrayList<Sensor> sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mToolbar = (Toolbar) findViewById(R.id.sensor_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("חזור");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DatabaseReference sensors = FirebaseDatabase.getInstance().getReference().child("Buildings");
        sensors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sensorList = collectSensors((Map<String, Object>) dataSnapshot.getValue());
                mRecyclerView = findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(SensorActivity.this);
                mAdapter = new SensrorAdapter(sensorList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public ArrayList<Sensor> collectSensors(Map<String, Object> sens) {
        ArrayList<Sensor> senList = new ArrayList<>();
        String name="";


        for (Map.Entry<String, Object> entry : sens.entrySet()) {

            Map<String,Object> singleSensor = (Map) entry.getValue();
            name = entry.getKey();

            Set<String> keys = singleSensor.keySet();
            String[] array = keys.toArray(new String[keys.size()]);
            String n1="",n2="",n3="",n4="";
            for(int i=0;i<array.length;i++){

                if(i==0){
                    n1=array[i];
                }else if(i==1)
                {
                    n2=array[i];
                }
                else if(i==2)
                {
                    n3=array[i];
                }
                else if(i==3)
                {
                    n4=array[i];
                }
            }
            senList.add(new Sensor(name,n1, n2, n3,n4));


        }

        return senList;
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
