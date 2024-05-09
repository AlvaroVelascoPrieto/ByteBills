package com.example.bytebills.ui.addStock;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.controller.AddStockToUserWorker;
import com.example.bytebills.controller.RegisterWorker;
import com.example.bytebills.databinding.FragmentAddStockBinding;
import com.example.bytebills.databinding.FragmentLoginBinding;
import com.example.bytebills.ui.registration.RegistrationFragment;

import java.util.Calendar;

public class AddStockFragment extends Fragment {

    private FragmentAddStockBinding binding;

    String TAG = "AddStockFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddStockBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Spinner symbol = binding.symbol;
        final Button addstockbtn = binding.addstockbtn;



        addstockbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View v) {
                String username = MainActivity.username;
                if (username != null) {
                    Data data = new Data.Builder()
                            .putString("symbol", symbol.toString())
                            .putString("username", username)
                            .build();

                    OneTimeWorkRequest addStockToUserWork =
                            new OneTimeWorkRequest.Builder(AddStockToUserWorker.class)
                                    .setInputData(data)
                                    .build();

                    WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(addStockToUserWork.getId())
                            .observe(AddStockFragment.this, status -> {
                                if (status != null && status.getState().isFinished()) {
                                    String addStockStatus = status.getOutputData().getString("status");
                                    try {
                                        if (addStockStatus.equals("Ok")) {
                                            //TODO: Added Stock to user
                                        } else {
                                            Toast.makeText(getActivity(), addStockStatus, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (NullPointerException e) {
                                        Toast.makeText(getActivity(), "An unexpected error ocurred, try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    WorkManager.getInstance(requireContext()).enqueue(addStockToUserWork);
                } else {
                    Toast.makeText(getActivity(), "user is null", Toast.LENGTH_SHORT).show();
                }
            }

        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}