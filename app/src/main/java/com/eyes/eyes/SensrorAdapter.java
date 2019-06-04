package com.eyes.eyes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SensrorAdapter extends RecyclerView.Adapter<SensrorAdapter.SensorViewHolder> {

    private ArrayList<Sensor>  mSensorList;

    public static  class SensorViewHolder extends  RecyclerView.ViewHolder{

        public TextView name;
        public TextView neig1;
        public TextView neig2;
        public TextView neig3;
        public TextView neig4;


        public SensorViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            neig1 = itemView.findViewById(R.id.neig1);
            neig2 = itemView.findViewById(R.id.neig2);
            neig3 = itemView.findViewById(R.id.neig3);
            neig4 = itemView.findViewById(R.id.neig4);

        }
    }

    public SensrorAdapter(ArrayList<Sensor> sensorList){
        mSensorList = sensorList;
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_item,parent,false);
        SensorViewHolder evh = new SensorViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(SensorViewHolder holder, int position) {

        Sensor currentSensor = mSensorList.get(position);

        holder.name.setText(currentSensor.getName());
        holder.neig1.setText(currentSensor.neig1);
        holder.neig2.setText(currentSensor.neig2);
        holder.neig3.setText(currentSensor.neig3);
        holder.neig4.setText(currentSensor.neig4);


    }

    @Override
    public int getItemCount() {
        return mSensorList.size();
    }
}
