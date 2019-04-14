package com.eyes.eyes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private ArrayList<LastConected> mLastConnected;


    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;
        public TextView first;
        public TextView last;

        public UsersViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            first = itemView.findViewById(R.id.first);
            last = itemView.findViewById(R.id.last);
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
        UsersAdapter.UsersViewHolder lcvh = new UsersAdapter.UsersViewHolder(v);
        return lcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersViewHolder holder, int position) {

        LastConected current = mLastConnected.get(position);
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
