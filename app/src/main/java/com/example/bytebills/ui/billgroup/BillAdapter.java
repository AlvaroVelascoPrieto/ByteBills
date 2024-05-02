package com.example.bytebills.ui.billgroup;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bytebills.R;

import java.util.ArrayList;
public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    //________create number of objects according to your need
    private ArrayList<String> titleDataList;
    boolean isHorizontalList;

    //________create  constructor with required parameter
    public BillAdapter(ArrayList<String> titleDataList, boolean isHorizontalList) {

        //________initialize
        this.titleDataList = titleDataList;
        this.isHorizontalList = isHorizontalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        //________show data in the horizontal listing
        if (isHorizontalList) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_group_member_list, parent, false);
        }

        //________show data in the vertical listing
        else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_group_list, parent, false);
        }

        //________return child view
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //________set data to the objects of each row widget
        holder.tvTitle.setText(titleDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return titleDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //________create objects of each row widget
        public TextView tvTitle;
        public ViewHolder(View view) {
            super(view);

            //________initialize objects of each row widget
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }
}