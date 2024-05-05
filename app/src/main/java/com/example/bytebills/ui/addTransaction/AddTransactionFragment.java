package com.example.bytebills.ui.addTransaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bytebills.R;
import com.example.bytebills.databinding.FragmentAddTransactionBinding;

import java.util.Calendar;

public class AddTransactionFragment extends AppCompatActivity {

    private FragmentAddTransactionBinding binding;
    private EditText dateEdt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_add_transaction);

        final EditText quantity = findViewById(R.id.stockQuantity);
        final EditText price = findViewById(R.id.price);
        dateEdt = findViewById(R.id.idEdtDate);
        final Button addTransactionBtn = findViewById(R.id.addTransactionBtn);

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(AddTransactionFragment.this);
            }
        });



        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add transaction to stock in user in DB
            }

        });



    }



    private void showDatePicker(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the selected year
                        String date = String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(dayOfMonth);
                        dateEdt.setText(date);
                    }
                }, year, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}