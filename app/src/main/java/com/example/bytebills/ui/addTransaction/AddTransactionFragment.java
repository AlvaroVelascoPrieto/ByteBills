package com.example.bytebills.ui.addTransaction;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivity;

import static com.example.bytebills.ui.stockvalue.StockValueActivity.stock_id;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.controller.AddDividendToUserWorker;
import com.example.bytebills.controller.AddTransactionToUserWorker;
import com.example.bytebills.controller.LoginWorker;
import com.example.bytebills.databinding.FragmentAddTransactionBinding;
import com.example.bytebills.ui.login.LoginFragment;

import java.util.Calendar;

public class AddTransactionFragment extends AppCompatActivity {

    String TAG = "AddTransactionFragment";
    private FragmentAddTransactionBinding binding;
    private EditText dateEdt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_add_transaction);
        final EditText quantity = findViewById(R.id.stockQuantity);
        final EditText price = findViewById(R.id.price);
        dateEdt = findViewById(R.id.idEdtDate);
        final Button addTransactionBtn = findViewById(R.id.addTransactionBtn);
        final RadioGroup transactionRadioGroup = findViewById(R.id.radioGroupTransaction);
        final RadioButton dividendTransaction = findViewById(R.id.dividendRB);
        final RadioButton purchaseTransaction = findViewById(R.id.purchaseRB);

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(AddTransactionFragment.this);
            }
        });



        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transactionRadioGroup.getCheckedRadioButtonId()==R.id.purchaseRB){
                    if (quantity.getText().length() > 0 && price.getText().length() > 0 && dateEdt.getText().length() > 0) {
                        String username = MainActivity.username;

                        Data data = new Data.Builder()
                                .putString("username", username)
                                .putString("symbol", stock_id)
                                .putString("price", price.getText().toString())
                                .putString("quantity", quantity.getText().toString())
                                .putString("timestamp", dateEdt.getText().toString())
                                .build();

                        OneTimeWorkRequest addTransactionWork =
                                new OneTimeWorkRequest.Builder(AddTransactionToUserWorker.class)
                                        .setInputData(data)
                                        .build();

                        WorkManager.getInstance(AddTransactionFragment.this).getWorkInfoByIdLiveData(addTransactionWork.getId())
                                .observe(AddTransactionFragment.this, status -> {
                                    if (status != null && status.getState().isFinished()) {
                                        String addTransactionStatus = status.getOutputData().getString("status");
                                        try {
                                            if (addTransactionStatus.equals("Ok")) { //El inicio de sesion es correcto
                                                //Correctly added
                                            } else {
                                                Toast.makeText(AddTransactionFragment.this, "Couldn't add, unexpected error", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                            Toast.makeText(AddTransactionFragment.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        WorkManager.getInstance(AddTransactionFragment.this).enqueue(addTransactionWork);
                    }
                } else if (transactionRadioGroup.getCheckedRadioButtonId()==R.id.dividendRB) {
                    Toast.makeText(AddTransactionFragment.this, "Dividend", Toast.LENGTH_SHORT).show();
                    if (price.getText().length() > 0 && dateEdt.getText().length() > 0) {
                        String username = MainActivity.username;

                        Data data = new Data.Builder()
                                .putString("username", username)
                                .putString("symbol", stock_id)
                                .putString("amount", price.getText().toString())
                                .putString("timestamp", dateEdt.getText().toString())
                                .build();

                        OneTimeWorkRequest addDividendWork =
                                new OneTimeWorkRequest.Builder(AddDividendToUserWorker.class)
                                        .setInputData(data)
                                        .build();

                        WorkManager.getInstance(AddTransactionFragment.this).getWorkInfoByIdLiveData(addDividendWork.getId())
                                .observe(AddTransactionFragment.this, status -> {
                                    if (status != null && status.getState().isFinished()) {
                                        String addTransactionStatus = status.getOutputData().getString("status");
                                        try {
                                            if (addTransactionStatus.equals("Ok")) { //El inicio de sesion es correcto
                                                //Correctly added
                                            } else {
                                                Toast.makeText(AddTransactionFragment.this, "Couldn't add, unexpected error", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                            Toast.makeText(AddTransactionFragment.this, "Network error, try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        WorkManager.getInstance(AddTransactionFragment.this).enqueue(addDividendWork);
                    }
                }

            }
        });

        dividendTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity.setVisibility(View.INVISIBLE);
                price.setHint("Amount");
            }
        });

        purchaseTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity.setVisibility(View.VISIBLE);
                price.setHint("Price");
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