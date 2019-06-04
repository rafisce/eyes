package com.eyes.eyes;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivityWorker extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH = 1000;
    private Toolbar mToolbar;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE2 = 2;
    private static final String KEY_ = "EYE#KEY1";


    private DatabaseReference current_user, current_user2;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private ImageButton power_off;
    private ImageButton lang_set;
    private ImageButton speaker;
    private ImageButton report;
    private ImageButton navigate_mic;
    private ImageButton user_docs;
    private ProgressDialog mProgress;
    private MediaRecorder mRecorder;
    private String myFile = null;
    private boolean recorded = false;
    private DesEncryption des = new DesEncryption();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_worker);
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        power_off = (ImageButton) findViewById(R.id.power_off);
        user_docs = (ImageButton) findViewById(R.id.user_docs);
        speaker = (ImageButton) findViewById(R.id.speaker);
        lang_set = (ImageButton) findViewById(R.id.lang_settings);
        mProgress = new ProgressDialog(this);

        myFile = Environment.getExternalStorageDirectory().getAbsolutePath();
        myFile += "/recorded_audio.3gp";
        speaker.setTag("on");
        report = (ImageButton) findViewById(R.id.report);
        navigate_mic = (ImageButton) findViewById(R.id.mic);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                Intent docsPageIntent = new Intent(MainActivityWorker.this, UserInfoActivity.class);
                startActivity(docsPageIntent);
            }
        });

        lang_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                temp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String lng = changeLanguage(dataSnapshot.child("language").getValue().toString());
                        DatabaseReference temp2 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                        temp2.child("language").setValue(lng);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        report.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                } else if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {

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

    public String changeLanguage(String language) {
        if (language.equals("eng")) {
            Toast.makeText(MainActivityWorker.this, "עברית", Toast.LENGTH_SHORT).show();
            return "heb";
        } else if (language.equals("heb")) {
            Toast.makeText(MainActivityWorker.this, "english", Toast.LENGTH_SHORT).show();
            return "eng";
        }
        return "eng";
    }

    private void startRecording() {
        if (ContextCompat.checkSelfPermission(MainActivityWorker.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

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
            if (ContextCompat.checkSelfPermission(MainActivityWorker.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                            ActivityCompat.requestPermissions(MainActivityWorker.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE);
                            ActivityCompat.requestPermissions(MainActivityWorker.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE2);
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
                            ActivityCompat.requestPermissions(MainActivityWorker.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE2);
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
            Toast.makeText(MainActivityWorker.this, "סיום הקלטה", Toast.LENGTH_SHORT).show();
            recorded = false;
        }


    }


    private void uploadAudio() {

        mProgress.setTitle("העלאה");
        mProgress.setMessage("אנא המתן בזמן שאנחנו מעלים את ההקלטה");
        mProgress.show();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Reports_counter");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long count = (long) dataSnapshot.getValue();
                        final StorageReference filepath = mStorage.child("audio_report").child(String.valueOf(count));
                        Uri uri = Uri.fromFile(new File(myFile));
                        count++;
                        DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Reports_counter");
                        temp.setValue(count);
                        final long finalCount = count;
                        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Got the uri
                                        final String tempUri = uri.toString();

                                        DatabaseReference tempUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                                        tempUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                DesEncryption des = new DesEncryption();
                                                String usr = des.Decrypt(dataSnapshot.child("user_name").getValue().toString(), KEY_);
                                                final String uri = tempUri;
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Reports").child(String.valueOf(finalCount - 1));
                                                ref.child("number").setValue(String.valueOf(finalCount));
                                                ref.child("uri").setValue(uri);
                                                ref.child("user").setValue(usr);
                                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
                                                String record_date = dateFormat.format(new Date());
                                                ref.child("record_date").setValue(record_date);
                                                mProgress.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }

    private void speak() {
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                if (dataSnapshot.child("language").getValue().toString().equals("heb")) {

                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "תאמר את היעד בבקשה");
                } else {

                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "say your destination please");
                }


                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH);
                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH: {
                if (resultCode == RESULT_OK && data != null) {
                    final ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    final String res = result.get(0);
                    if (checkDest(res)) {

                        DatabaseReference bui = FirebaseDatabase.getInstance().getReference().child("Buildings");
                        bui.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                Object value = map.get(res);
                                if (value != null) {
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
                                            current_user2.child("dest_list").child(String.valueOf(count)).setValue(res);
                                            Intent intent = new Intent(MainActivityWorker.this, NavigationActivity.class);
                                            intent.putExtra("destination", res);
                                            startActivity(intent);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // throw an error if setValue() is rejected
                                            throw databaseError.toException();
                                        }
                                    });


                                } else {
                                    Toast.makeText(MainActivityWorker.this, "יעד לא קיים", Toast.LENGTH_SHORT).show();
                                }  // <= Change to ++count

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // throw an error if setValue() is rejected
                                throw databaseError.toException();
                            }
                        });

                    }
                }
            }
            break;
        }
    }

    private void Logout() {
        Intent startPageIntent = new Intent(MainActivityWorker.this, StartPageActivity.class);
        startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startPageIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null || mAuth.getCurrentUser().getUid() == null) {
            Logout();
        }
    }

    @Override
    protected void onPause() {
        current_user = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
        current_user.child("online").setValue("false");
        super.onPause();
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
    public boolean checkDest(String str) {
        return !str.equals("");
    }


}
