package com.example.bytebills.ui.addTransaction;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.controller.AddTransactionToUserWorker;
import com.example.bytebills.controller.LoginWorker;
import com.example.bytebills.databinding.FragmentAddTransactionBinding;
import com.example.bytebills.ui.login.LoginFragment;

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
                String username = MainActivity.username;
                Data data = new Data.Builder()
                        .putString("username", username)
                        //TODO: get symbol from the stock tapped
                        .putFloat("price", Float.parseFloat(price.getText().toString()))
                        .putFloat("quantity", Float.parseFloat(quantity.getText().toString()))
                        //TODO: get timestamp de la fecha
                        .build();

                OneTimeWorkRequest addTransactionWork =
                        new OneTimeWorkRequest.Builder(AddTransactionToUserWorker.class)
                                .setInputData(data)
                                .build();

                WorkManager.getInstance(AddTransactionFragment.this).getWorkInfoByIdLiveData(addTransactionWork.getId())
                        .observe(AddTransactionFragment.this, status -> {
                            if (status != null && status.getState().isFinished()) {
                                String loginStatus = status.getOutputData().getString("status");
                                try {
                                    if (loginStatus.equals("Ok")) { //El inicio de sesion es correcto
                                        MainActivity.username = username;

                                        Intent i = new Intent(AddTransactionFragment.this, MainActivity.class);
                                        i.putExtra("username", username);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(AddTransactionFragment.this, "Username or password are incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    Toast.makeText(AddTransactionFragment.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                WorkManager.getInstance(AddTransactionFragment.this).enqueue(addTransactionWork);
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