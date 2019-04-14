package com.eyes.eyes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class LastConnectedAdapter extends RecyclerView.Adapter<LastConnectedAdapter.LastConnectedViewHolder> {
    private ArrayList<LastConected> mLastConnected;

    public static class LastConnectedViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView time;

        public LastConnectedViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
        }
    }

    public LastConnectedAdapter(ArrayList<LastConected> lastConecteds) {
        mLastConnected = lastConecteds;
    }


    @NonNull
    @Override
    public LastConnectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_connected, parent, false);
        LastConnectedViewHolder lcvh = new LastConnectedViewHolder(v);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LastConnectedViewHolder holder, int position) {
        LastConected current = mLastConnected.get(position);

        holder.name.setText("name: " + current.getName());
        holder.time.setText("last connected: " + current.getTime());
    }

    @Override
    public int getItemCount() {
        return mLastConnected.size();
    }
}
