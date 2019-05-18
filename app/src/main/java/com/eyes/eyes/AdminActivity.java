package com.eyes.eyes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

public class AdminActivity extends AppCompatActivity {

    private Button lastConnected;
    private Button reports;
    private Button logout;
    private Button users;
    private Button dests;
    private Button create_worker;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        lastConnected = (Button) findViewById(R.id.last_connected);
        reports = (Button) findViewById(R.id.reports);
        logout = (Button) findViewById(R.id.logout);
        dests = (Button) findViewById(R.id.dests_report_admin);
        users = (Button) findViewById(R.id.users);
        create_worker = (Button) findViewById(R.id.create_worker);

        lastConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lc = new Intent(AdminActivity.this, LastConnectedActivity.class);
                startActivity(lc);
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ra = new Intent(AdminActivity.this, ReportsActivity.class);
                startActivity(ra);
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent us = new Intent(AdminActivity.this, UsersActivity.class);
                startActivity(us);
            }
        });

        create_worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cw = new Intent(AdminActivity.this, CreateWorker.class);
                startActivity(cw);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent startPageIntent = new Intent(AdminActivity.this, StartPageActivity.class);
                startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startPageIntent);
                finish();
            }
        });

        dests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dr = new Intent(AdminActivity.this, DestinationReportActivity.class);
                startActivity(dr);
            }
        });
    }


}
