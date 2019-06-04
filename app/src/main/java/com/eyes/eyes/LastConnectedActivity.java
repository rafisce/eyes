package com.eyes.eyes;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
    private LastConnectedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String KEY_ = "EYE#KEY1";

    private Toolbar mToolbar;

    private ArrayList<LastConected> userList = new ArrayList<>();

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
                        userList = collectUsers((Map<String, Object>) dataSnapshot.getValue());

                        mRecyclerView = findViewById(R.id.recyclerView);
                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(LastConnectedActivity.this);
                        mAdapter = new LastConnectedAdapter(userList);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);


                        mAdapter.setOnItemClickListener(new LastConnectedAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                            }

                            @Override
                            public void onActivation(final int position) {

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(LastConnectedActivity.this);
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(LastConnectedActivity.this);
                                builder2.setCancelable(true);
                                builder1.setCancelable(true);
                                if(userList.get(position).isActive().equals("true"))
                                {
                                    builder1.setMessage("ביטול משתמש!");
                                    builder1.setPositiveButton(
                                            "כן",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    userList.get(position).setActive("false");
                                                    DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(userList.get(position).getUid());
                                                    current_user.child("active").setValue("false");
                                                    mAdapter.notifyDataSetChanged();
                                                    dialog.cancel();
                                                }
                                            });

                                    builder1.setNegativeButton(
                                            "לא",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                                else
                                {
                                    builder2.setMessage("הפעלת משתמש!");
                                    builder2.setPositiveButton(
                                            "כן",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    userList.get(position).setActive("true");
                                                    DatabaseReference current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(userList.get(position).getUid());
                                                    current_user.child("active").setValue("true");
                                                    mAdapter.notifyDataSetChanged();
                                                    dialog.cancel();
                                                }
                                            });

                                    builder2.setNegativeButton(
                                            "לא",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert12 = builder2.create();
                                    alert12.show();

                                }

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    public ArrayList<LastConected> collectUsers(Map<String, Object> users) {

        ArrayList<LastConected> useList = new ArrayList<>();
        DesEncryption des = new DesEncryption();
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            if (isWorker(des.Decrypt((String) singleUser.get("user_type"),KEY_))) {
                useList.add(new LastConected(des.Decrypt((String) singleUser.get("user_name"),KEY_), (String) singleUser.get("last_connected"),(String) singleUser.get("active"),(String)singleUser.get("online"), entry.getKey().toString()));
            }
            Collections.sort(useList, new Comparator<LastConected>() {
                public int compare(LastConected m1, LastConected m2) {
                    return m1.getTime().compareTo(m2.getTime());
                }

            });
            Collections.reverse(useList);

        }
        return useList;
    }

    boolean isWorker(String str){
        return str.equals("worker");
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

