package com.eyes.eyes;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private ArrayList<LastConected> mLastConnected;
    public OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onActivation(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;
        public TextView first;
        public TextView last;
        public ImageView activationImage;
        public RelativeLayout layout;

        public UsersViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            first = itemView.findViewById(R.id.first);
            last = itemView.findViewById(R.id.last);
            activationImage = itemView.findViewById(R.id.deactviate_user);
            layout = itemView.findViewById(R.id.users_layout);

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

    public UsersAdapter(ArrayList<LastConected> lastConecteds) {
        mLastConnected = lastConecteds;
        Iterator<LastConected> iterator = mLastConnected.iterator();
        while(iterator.hasNext()) {
            LastConected next = iterator.next();
            if(next.getType().equals("admin")) {
                iterator.remove();
            }
        }

    }

    @NonNull
    @Override

    public UsersAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        UsersAdapter.UsersViewHolder lcvh = new UsersAdapter.UsersViewHolder(v,mListener);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersViewHolder holder, int position) {

        LastConected current = mLastConnected.get(position);
        if(current.isActive().equals("true")){
            holder.activationImage.setImageResource(R.drawable.user_red);
            holder.layout.setBackgroundColor(Color.parseColor("#c2f0c2"));
        }
        else
        {
            holder.activationImage.setImageResource(R.drawable.user_green);
            holder.layout.setBackgroundColor(Color.parseColor("#ffc2b3"));
        }
        holder.name.setText("name: " + current.getName());
        holder.email.setText("email: " + current.getEmail());
        holder.first.setText("joined on: " + current.getJoined());
        holder.last.setText("last connected :" + current.getTime());

    }

    @Override
    public int getItemCount() {
        return mLastConnected.size();
    }
}
