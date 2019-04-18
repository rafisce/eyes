package com.eyes.eyes;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class ReportsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ReportAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Toolbar mToolbar;
    private ArrayList<Report> reportList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        mToolbar = (Toolbar) findViewById(R.id.reports_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("מנהל");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DatabaseReference refUri = FirebaseDatabase.getInstance().getReference().child("Reports");
        refUri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                reportList = collectReports((Map<String, Object>) dataSnapshot.getValue());

                mRecyclerView = findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(ReportsActivity.this);
                mAdapter = new ReportAdapter(reportList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new ReportAdapter.OnItemClickListener() {
                    @Override
                    public void OnPlayClick(int position) {
                        playAudio(reportList.get(position).getUri());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public ArrayList<Report> collectReports(Map<String, Object> reps) {
        ArrayList<Report> repList = new ArrayList<>();
        reps.remove(reps.get("@"));
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : reps.entrySet()) {

            Map singleReport = (Map) entry.getValue();

            //Get phone field and append to list

            repList.add(new Report((String) singleReport.get("user"), (String) singleReport.get("number"), (String) singleReport.get("uri"), (String) singleReport.get("record_date")));

            Collections.sort(repList, new Comparator<Report>() {
                public int compare(Report m1, Report m2) {
                    return m1.getNumber().compareTo(m2.getNumber());
                }

            });
//            Collections.reverse(repList);
        }

        return repList;
    }


    public void playAudio(String uri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}