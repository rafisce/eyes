package com.eyes.eyes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private ArrayList<Report> mReports;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void OnPlayClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public TextView name;
        public TextView date;
        public ImageView check;
        private ImageView play;


        public ReportViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            play = itemView.findViewById(R.id.play);
            check = itemView.findViewById(R.id.checked);


            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.OnPlayClick(pos);
                        }
                    }
                }
            });
        }
    }

    public ReportAdapter(ArrayList<Report> reports) {
        mReports = reports;
    }


    @NonNull
    @Override
    public ReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        ReportAdapter.ReportViewHolder rvh = new ReportAdapter.ReportViewHolder(v, mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ReportViewHolder holder, int position) {
        Report current = mReports.get(position);
        holder.name.setText(current.getName());
        holder.date.setText(current.getDate());
        holder.number.setText(current.getNumber()+".");


    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }
}
