package com.eyes.eyes;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.eyes.eyes.Dijkstra.Dijkstra;
import com.eyes.eyes.Dijkstra.Edge;
import com.eyes.eyes.Dijkstra.Vertex;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NavigationActivity extends AppCompatActivity {
    private ImageView imgView;
    private Button cancel;
    private DatabaseReference sensors;
    private String dest=null;
    int[] imageArray = { R.drawable.right, R.drawable.left, R.drawable.up,R.drawable.checked};
    int count=0;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        if (getIntent().hasExtra("destination")) {
            dest = getIntent().getStringExtra("destination");
        }

        imgView = (ImageView) findViewById(R.id.direction);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        DatabaseReference refUri = FirebaseDatabase.getInstance().getReference().child("Buildings");
        refUri.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dest!=null) {
                    navigate((Map<String, Object>) dataSnapshot.getValue(), dest);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void navigate(Map<String, Object> reps, String b) {
        ArrayList<Vertex> vertexs = new ArrayList<>();
        reps.remove(reps.get("@"));
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : reps.entrySet()) {
            String name = entry.getKey();
            Map<String, Object> singleVertex = (Map) entry.getValue();
            Vertex v = new Vertex(name);
            vertexs.add(v);

        }
        for (Map.Entry<String, Object> entry : reps.entrySet()) {
            Map<String, Object> singleVertex = (Map) entry.getValue();

            for (Vertex ver : vertexs) {
                if (ver.toString().equals(entry.getKey())) {
                    for (Map.Entry<String, Object> entry2 : singleVertex.entrySet()) {
                        Map<String, Object> single = (Map) entry2.getValue();
                        String name = entry2.getKey();
                        ver.addNeighbour(new Edge((Long) single.get("distance"), ver, getVer(name, vertexs), (String) single.get("direction")));
                    }
                }
            }
        }
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.computePath(getVer("white", vertexs));

        final List<Vertex> path = dijkstra.getShortestPathTo(getVer(b, vertexs));
        final Map<Integer,Map<String,String>> path2 = new HashMap<Integer, Map<String, String>>();

        for(int i=0;i<path.size();i++){
            if(i!=path.size()-1){
                String name=path.get(i+1).toString();
                final String dir = path.get(i).getEdge(name).getDir();
                final Double distance = path.get(i).getEdge(name).getWeight();
                path2.put(i,new HashMap<String,String>() {{
                    put("distance", Double.toString(distance));
                    put("direction",dir);
                }});
            }
        }

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                Double dist = Double.parseDouble(path2.get(count).get("distance"));
                String dire = path2.get(count).get("direction");
                if(dire.equals("right")){
                    System.out.println("right");
                    imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), imageArray[0], 100, 100));
                }
                else if(dire.equals("left"))
                {
                    System.out.println("left");
                    imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), imageArray[1], 100, 100));
                }
                else
                {
                    System.out.println("up");
                    imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), imageArray[2], 100, 100));
                }

                count++;

                handler.postDelayed(this, 2000*(long)Math.floor(dist + 0.5d));
                if(count>path2.size()-1)
                {
                    imgView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), imageArray[3], 100, 100));
                    handler.removeCallbacksAndMessages(null);
                }//for interval...
            }
        };
        handler.postDelayed(runnable, 1000);


    }

    public Vertex getVer(String name, ArrayList<Vertex> vertexList) {

        for (Vertex ver : vertexList) {
            if (ver.toString().equals(name)) {
                return ver;
            }
        }
        return null;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                          int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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


