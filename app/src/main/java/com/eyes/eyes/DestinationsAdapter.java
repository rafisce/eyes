package com.eyes.eyes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsAdapter.DestinationsViewHolder>{

    private ArrayList<Destination> mDestinations;

    public static class DestinationsViewHolder extends RecyclerView.ViewHolder {
        public TextView dest;


        public DestinationsViewHolder(View itemView) {
            super(itemView);
            dest = itemView.findViewById(R.id.dest);

        }
    }

    public DestinationsAdapter(ArrayList<Destination> destinations) {
        mDestinations = destinations;
    }


    @NonNull
    @Override
    public DestinationsAdapter.DestinationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.destination_item, parent, false);
        DestinationsAdapter.DestinationsViewHolder dvh = new DestinationsAdapter.DestinationsViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationsAdapter.DestinationsViewHolder holder, int position) {
        Destination current = mDestinations.get(position);
        holder.dest.setText(current.getDest()+". "+current.getNum());
    }

    @Override
    public int getItemCount() {
        return mDestinations.size();
    }
}
