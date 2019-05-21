package com.eyes.eyes;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LastConnectedAdapter extends RecyclerView.Adapter<LastConnectedAdapter.LastConnectedViewHolder> {
    private ArrayList<LastConected> mLastConnected;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onActivation(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class LastConnectedViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView time;
        public ImageView activationImage;
        public RelativeLayout layout;


        public LastConnectedViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            activationImage = itemView.findViewById(R.id.deactviate_worker);
            layout = itemView.findViewById(R.id.last_connected_layout);

            activationImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onActivation(position);

                        }
                    }
                }
            });
        }

    }


    public LastConnectedAdapter(ArrayList<LastConected> lastConecteds) {
        mLastConnected = lastConecteds;
    }


    @NonNull
    @Override
    public LastConnectedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_connected, parent, false);
        LastConnectedViewHolder lcvh = new LastConnectedViewHolder(v, mListener);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LastConnectedViewHolder holder, int position) {
        LastConected current = mLastConnected.get(position);
        if (current.isActive().equals("true")) {
            holder.activationImage.setImageResource(R.drawable.user_red);
            holder.layout.setBackgroundColor(Color.parseColor("#c2f0c2"));
        } else {
            holder.activationImage.setImageResource(R.drawable.user_green);
            holder.layout.setBackgroundColor(Color.parseColor("#ffc2b3"));
        }

        holder.name.setText("name: " + current.getName());
        holder.time.setText("last connected: " + current.getTime());
    }


    @Override
    public int getItemCount() {
        return mLastConnected.size();
    }




}
