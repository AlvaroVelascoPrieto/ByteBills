package com.example.bytebills.ui.addStock;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.databinding.FragmentAddStockBinding;
import com.example.bytebills.databinding.FragmentLoginBinding;
import com.example.bytebills.ui.registration.RegistrationFragment;

import java.util.Calendar;

public class AddStockFragment extends Fragment {

    private FragmentAddStockBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddStockBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Spinner symbol = binding.symbol;
        final Button addstockbtn = binding.addstockbtn;



        addstockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add stock to user in DB
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