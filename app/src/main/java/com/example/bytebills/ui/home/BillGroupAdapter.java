package com.example.bytebills.ui.home;

import static android.app.PendingIntent.getActivity;

import static androidx.core.content.ContextCompat.startActivities;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.model.BillGroup;
import com.example.bytebills.ui.billgroup.BillFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillGroupAdapter extends RecyclerView.Adapter<BillGroupAdapter.BillGroupViewHolder> {
    private final List<BillGroup> billGroupList;
    private OnDeleteClickListener onDeleteClickListener;

    public BillGroupAdapter(List<BillGroup> billGroupList) {
        this.billGroupList = billGroupList;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public static class BillGroupViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBillGroupTitle;
        TextView textViewBillGroupDescription;
        TextView textViewLastUpdateDate;
        Button buttonDeleteBillGroup;

        public BillGroupViewHolder(View itemView) {
            super(itemView);
            textViewBillGroupTitle = itemView.findViewById(R.id.textViewBillGroupTitle);
            textViewBillGroupDescription = itemView.findViewById(R.id.textViewBillGroupDesc);
            textViewLastUpdateDate = itemView.findViewById(R.id.textViewBillDate);
            buttonDeleteBillGroup = itemView.findViewById(R.id.buttonDeleteBillGroup);
        }
    }

    @NonNull
    @Override
    public BillGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_group_card, parent, false);
        return new BillGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillGroupViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BillGroup currentBillGroup = billGroupList.get(position);

        holder.textViewBillGroupTitle.setText(currentBillGroup.getTitle());
        holder.textViewBillGroupDescription.setText(currentBillGroup.getDescription());

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
        holder.buttonDeleteBillGroup.setOnClickListener(new View.OnClickListener() {
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
                        final int id = currentBillGroup.getId();
                        //TODO: delete billgroup with id from DB
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
                //TODO: Open fragment for BillGroupDisplay
                Intent i = new Intent(holder.itemView.getContext(), BillFragment.class);
                startActivity(holder.itemView.getContext(),i, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return billGroupList.size();
    }
}