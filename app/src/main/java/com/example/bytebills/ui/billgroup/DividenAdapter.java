package com.example.bytebills.ui.billgroup;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bytebills.R;

import java.util.ArrayList;
public class DividenAdapter extends RecyclerView.Adapter<DividenAdapter.ViewHolder> {

    //________create number of objects according to your need
    private ArrayList<String> dividendDateList;
    private ArrayList<String> dividendValueList;
    private ArrayList<String> dividendPercentageList;
    boolean isHorizontalList;

    //________create  constructor with required parameter
    public DividenAdapter(ArrayList<String> dividendDateList,ArrayList<String> dividendValueList,ArrayList<String> dividendPercentageList) {
        this.dividendDateList = dividendDateList;
        this.dividendValueList = dividendValueList;
        this.dividendPercentageList = dividendPercentageList;
        this.isHorizontalList = isHorizontalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        //________show data in the horizontal listing

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.divident_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //________set data to the objects of each row widget
        holder.tvDate.setText(dividendDateList.get(position));
        holder.tvValue.setText(dividendValueList.get(position));
        holder.tvPercentage.setText(dividendPercentageList.get(position));
    }

    @Override
    public int getItemCount() {
        return dividendDateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //________create objects of each row widget
        public TextView tvDate;
        public TextView tvValue;
        public TextView tvPercentage;

        public ViewHolder(View view) {
            super(view);

            //________initialize objects of each row widget
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvValue = (TextView) view.findViewById(R.id.tvMoney);
            tvPercentage = (TextView) view.findViewById(R.id.tvPercentage);

        }
    }
}