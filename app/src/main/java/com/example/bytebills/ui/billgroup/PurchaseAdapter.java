package com.example.bytebills.ui.billgroup;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bytebills.R;

import java.util.ArrayList;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {

    //________create number of objects according to your need
    private ArrayList<String> purchaseDateList;
    private ArrayList<String> purchasePriceList;
    private ArrayList<String> purchaseValueList;
    private ArrayList<String> currentValueList;
    private ArrayList<String> differenceList;
    private ArrayList<String> operationStateList;

    //________create  constructor with required parameter
    public PurchaseAdapter(ArrayList<String> purchaseDateList, ArrayList<String> purchasePriceList, ArrayList<String> purchaseValueList, ArrayList<String> currentValueList, ArrayList<String> differenceList, ArrayList<String> operationStateList) {
        this.purchaseDateList = purchaseDateList;
        this.purchasePriceList = purchasePriceList;
        this.purchaseValueList = purchaseValueList;
        this.currentValueList = currentValueList;
        this.differenceList = differenceList;
        this.operationStateList = operationStateList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        //________show data in the horizontal listing

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_adapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //________set data to the objects of each row widget
        holder.tvDate.setText(purchaseDateList.get(position));
        holder.tvPurchasePrice.setText(purchasePriceList.get(position));
        holder.tvPurchaseValue.setText(purchaseValueList.get(position));
        holder.tvCurrentValue.setText(currentValueList.get(position));
        holder.tvDifference.setText(differenceList.get(position));
        holder.tvOperationState.setText(operationStateList.get(position));
    }

    @Override
    public int getItemCount() {
        return purchaseDateList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //________create objects of each row widget
        public TextView tvDate;
        public TextView tvPurchasePrice;
        public TextView tvPurchaseValue;
        public TextView tvCurrentValue;
        public TextView tvDifference;
        public TextView tvOperationState;

        public ViewHolder(View view) {
            super(view);

            //________initialize objects of each row widget
            tvDate = (TextView) view.findViewById(R.id.tvPurchaseDateValue);
            tvPurchasePrice = (TextView) view.findViewById(R.id.tvPurchasePriceValue);
            tvPurchaseValue = (TextView) view.findViewById(R.id.tvPurchaseValueValue);
            tvCurrentValue = (TextView) view.findViewById(R.id.tvCurrentValueValue);
            tvDifference = (TextView) view.findViewById(R.id.tvDiffValue);
            tvOperationState = (TextView) view.findViewById(R.id.tvOperationStateValue);

        }
    }
}