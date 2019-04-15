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


public class ReportsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ReportAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Toolbar mToolbar;

    private ArrayList<Report> repList = new ArrayList<>();
   // private ArrayList<String> DownloadUris = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        mToolbar = (Toolbar) findViewById(R.id.reports_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("מנהל");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        DatabaseReference refUri = FirebaseDatabase.getInstance().getReference().child("audio_uri");
        refUri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                collectReports((ArrayList<String>) dataSnapshot.getValue());

                mRecyclerView = findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(ReportsActivity.this);
                mAdapter = new ReportAdapter(repList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new ReportAdapter.OnItemClickListener() {
                    @Override
                    public void OnPlayClick(int position) {
                        playAudio(repList.get(position).getUri());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void collectReports(ArrayList<String> reps) {

        int count = reps.size();
        //iterate through each user, ignoring their UID
        for (String entry : reps) {

            //Get user map
            String rep = entry;
            //Get phone field and append to list
            repList.add(new Report(String.valueOf(count - 1), rep));
            count--;
            Collections.sort(repList, new Comparator<Report>() {
                public int compare(Report m1, Report m2) {
                    return m1.name.compareTo(m2.name);
                }
            });


        }
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