package com.eyes.eyes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class WorkerActivity extends AppCompatActivity {

    private Button as_user;
    private Button info;
    private Button logout;
    private Button dests;
    private Button reports;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        as_user = (Button) findViewById(R.id.user_worker);
        reports = (Button) findViewById(R.id.worker_user_reports);
        info = (Button) findViewById(R.id.my_details);
        logout = (Button) findViewById(R.id.worker_logout);
        dests = (Button) findViewById(R.id.worker_dest);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startPageIntent = new Intent(WorkerActivity.this, StartPageActivity.class);
                startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startPageIntent);
                finish();
            }
        });


        as_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(WorkerActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ra = new Intent(WorkerActivity.this, WorkerReportActivity.class);
                startActivity(ra);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent docsPageIntent = new Intent(WorkerActivity.this, WorkerInfoActivity.class);
                startActivity(docsPageIntent);
            }
        });

        dests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent docsPageIntent = new Intent(WorkerActivity.this, DestinationReportActivity.class);
                startActivity(docsPageIntent);
            }
        });




    }
}
