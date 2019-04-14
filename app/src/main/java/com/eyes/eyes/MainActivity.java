package com.eyes.eyes;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.os.Looper.prepare;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH = 1000;
    private Toolbar mToolbar;

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE2 = 2;

    private DatabaseReference current_user,current_user2;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private ImageButton power_off;
    private ImageButton speaker;
    private ImageButton report;
    private ImageButton navigate_mic;
    private ImageButton user_docs;
    private ProgressDialog mProgress;
    private MediaRecorder mRecorder;
    private String myFile = null;
    private boolean recorded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        power_off = (ImageButton) findViewById(R.id.power_off);
        user_docs = (ImageButton) findViewById(R.id.user_docs);
        speaker = (ImageButton) findViewById(R.id.speaker);
        mProgress = new ProgressDialog(this);

        myFile = Environment.getExternalStorageDirectory().getAbsolutePath();
        myFile += "/recorded_audio.3gp";
        speaker.setTag("on");
        report = (ImageButton) findViewById(R.id.report);
        navigate_mic = (ImageButton) findViewById(R.id.mic);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        power_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Logout();
            }
        });

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((String) speaker.getTag() == "on") {
                    speaker.setImageResource(R.drawable.speaker_off);
                    speaker.setTag("off");
                    AudioManager amanager = (AudioManager) getSystemService(MainActivity.AUDIO_SERVICE);
                    amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                } else if ((String) speaker.getTag() == "off") {
                    speaker.setImageResource(R.drawable.speaker_on);
                    speaker.setTag("on");
                    AudioManager amanager = (AudioManager) getSystemService(MainActivity.AUDIO_SERVICE);
                    amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                }
            }
        });

        user_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docsPageIntent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(docsPageIntent);

            }
        });

        report.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                }

                return false;
            }


        });

        navigate_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });


    }

    private void startRecording() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(myFile);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("recording failed", "prepare() failed");
            }
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mRecorder.start();
                recorded = true;
            } else {
                requestWriteExternalStoragePermission();
            }

        } else {
            requestRecordAudioPermission();

        }


    }

    private void requestRecordAudioPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            new AlertDialog.Builder(this)
                    .setTitle("צורך בהרשאה")
                    .setMessage("האפליקציה צריכה הרשאה להקלטת שמע ובאחסון")
                    .setPositiveButton("אשר", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE2);
                        }
                    })
                    .setNegativeButton("בטל", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
        }
    }

    private void requestWriteExternalStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("צורך בהרשאה")
                    .setMessage("האפליקציה צריכה הרשאה לאחסון")
                    .setPositiveButton("אשר", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE2);
                        }
                    })
                    .setNegativeButton("בטל", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE2);
        }
    }


    public void stopRecording() {

        if (recorded) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

            uploadAudio();
            Toast.makeText(MainActivity.this, "סיום הקלטה", Toast.LENGTH_SHORT).show();
            recorded = false;
        }


    }


    private void uploadAudio() {


        mProgress.setTitle("העלאה");
        mProgress.setMessage("אנא המתן בזמן שאנחנו מעלים את ההקלטה");
        mProgress.show();
        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        StorageReference filepath = mStorage.child("audio_report").child(mAuth.getCurrentUser().getUid()).child(date);
        Uri uri = Uri.fromFile(new File(myFile));

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();


            }
        });
    }

    private void speak() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "תאמר את היעד בבקשה");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH);
        } catch (Exception e) {

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH: {
                if (resultCode == RESULT_OK && data != null) {

                    Toast.makeText(MainActivity.this, "hjhj", Toast.LENGTH_LONG).show();
                    final ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final String res = result.get(0);

                    current_user2 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                    current_user2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            long count = (long) dataSnapshot.child("dest_counter").getValue();
                            count++;
                            current_user2.child("dest_counter").setValue(count);  // <= Change to ++count

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // throw an error if setValue() is rejected
                            throw databaseError.toException();
                        }
                    });
                    current_user2 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                    current_user2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            long count = (long) dataSnapshot.child("dest_counter").getValue();
                            current_user2.child("dest_list").child(String.valueOf(count)).setValue(res);  // <= Change to ++count

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // throw an error if setValue() is rejected
                            throw databaseError.toException();
                        }
                    });
                }
            }
            break;
        }
    }

    private void Logout() {
        Intent startPageIntent = new Intent(MainActivity.this, StartPageActivity.class);
        startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startPageIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null || mAuth.getCurrentUser().getUid() == null) {
            Logout();
        } else {

            String currentUserId = mAuth.getCurrentUser().getUid();
            DatabaseReference storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
            String last_connected = dateFormat.format(new Date());
            storeUserDefaultDataReference.child("last_connected").setValue(last_connected);

            current_user = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            current_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("user_type").getValue().toString().equals("admin")) {
                        Intent mainIntent = new Intent(MainActivity.this, AdminActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
