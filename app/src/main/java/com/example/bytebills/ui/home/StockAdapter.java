package com.example.bytebills.ui.home;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivities;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.controller.DeleteStockWorker;
import com.example.bytebills.model.BillGroup;
import com.example.bytebills.ui.stockvalue.StockValueActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.BillGroupViewHolder> {
    private final List<BillGroup> billGroupList;
    private OnDeleteClickListener onDeleteClickListener;

    public StockAdapter(List<BillGroup> billGroupList) {
        this.billGroupList = billGroupList;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public static class BillGroupViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStockName;
        TextView textViewStockId;
        TextView textViewLastUpdateDate;
        TextView textViewCurrentPrice;
        TextView textViewSessionDelta;
        Button buttonDeleteStock;

        public BillGroupViewHolder(View itemView) {
            super(itemView);
            textViewStockName = itemView.findViewById(R.id.textViewStockName);
            textViewStockId = itemView.findViewById(R.id.textViewStockId);
            textViewLastUpdateDate = itemView.findViewById(R.id.textViewUpdateDate);
            textViewCurrentPrice = itemView.findViewById(R.id.textViewCurrentPrice);
            textViewSessionDelta = itemView.findViewById(R.id.textViewSessionDelta);
            buttonDeleteStock = itemView.findViewById(R.id.buttonDeleteStock);
        }
    }

    @NonNull
    @Override
    public BillGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_card, parent, false);
        return new BillGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillGroupViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BillGroup currentBillGroup = billGroupList.get(position);

        holder.textViewStockName.setText(currentBillGroup.getDescription());
        holder.textViewStockId.setText(currentBillGroup.getTitle());
        holder.textViewCurrentPrice.setText(currentBillGroup.getCurrentPrice());
        Boolean sessionDelta = Float.valueOf(currentBillGroup.getSessionDelta()) > 0.0f;
        holder.textViewSessionDelta.setText(currentBillGroup.getSessionDelta() + "%");
        if (sessionDelta){
            holder.textViewSessionDelta.setTextColor(Color.parseColor("#008000"));
        }else {
            holder.textViewSessionDelta.setTextColor(Color.parseColor("#FF1100"));
        }

        // Format and display the due date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dueDate = dateFormat.parse(currentBillGroup.getLastUpdateDate());
            String formattedDate = dateFormat.format(dueDate);
            holder.textViewLastUpdateDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.textViewLastUpdateDate.setText("Invalid date format");
        }

        // Set click listener for delete button
        holder.buttonDeleteStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog before deleting the task
                String title = v.getContext().getString(R.string.confirm_delete_title);
                String message = v.getContext().getString(R.string.confirm_delete_message);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(title);
                builder.setMessage(message);
                String delete = v.getContext().getString(R.string.delete);
                builder.setPositiveButton(delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call onDeleteClick method to handle task deletion
                        String symbol = currentBillGroup.getTitle();
                        String username = MainActivity.username;
                        OneTimeWorkRequest deleteStockWork = new OneTimeWorkRequest.Builder(DeleteStockWorker.class)
                                .setInputData(new Data.Builder()
                                        .putString("username", username)
                                        .putString("symbol", symbol)
                                        .build())
                                .build();

                        WorkManager.getInstance().getWorkInfoByIdLiveData(deleteStockWork.getId())
                                .observe((LifecycleOwner) holder.itemView.getContext(), status -> {
                                    if (status != null && status.getState().isFinished()) {
                                        String deleteStockStatus = status.getOutputData().getString("status");
                                        try {
                                            if (deleteStockStatus.equals("Ok")) {
                                                billGroupList.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, billGroupList.size());
                                            }
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        WorkManager.getInstance().enqueue(deleteStockWork);
                    }
                });
                String cancel = v.getContext().getString(R.string.cancel);
                builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), StockValueActivity.class);
                i.putExtra("stockSymbol", holder.textViewStockId.getText());
                startActivity(holder.itemView.getContext(),i, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return billGroupList.size();
    }
}