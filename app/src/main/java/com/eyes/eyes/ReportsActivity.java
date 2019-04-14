package com.eyes.eyes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ReportsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Toolbar mToolbar;

    private ArrayList<Report> repList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        mToolbar = (Toolbar) findViewById(R.id.reports_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("מנהל");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void collectReports(Map<String,Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            repList.add(new Report((String)singleUser.get("user_name"),(String)singleUser.get("last_connected"),(String)singleUser.get("uri")));
            Collections.sort(repList, new Comparator<Report>() {
                public int compare(Report m1, Report m2) {
                    return m1.getTime().compareTo(m2.getTime());
                }
            });
            Collections.reverse(repList);

        }

    }
}
