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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.controller.AddStockToUserWorker;
import com.example.bytebills.controller.RegisterWorker;
import com.example.bytebills.controller.StockInfoWorker;
import com.example.bytebills.controller.SymbolnfoWorker;
import com.example.bytebills.databinding.FragmentAddStockBinding;
import com.example.bytebills.databinding.FragmentLoginBinding;
import com.example.bytebills.ui.registration.RegistrationFragment;
import com.example.bytebills.ui.stockvalue.StockValueActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AddStockFragment extends Fragment {

    private FragmentAddStockBinding binding;
    private JSONObject symbolJSON;

    String TAG = "AddStockFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddStockBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Spinner symbol = binding.symbol;
        final Button addstockbtn = binding.addstockbtn;
        final RadioButton cryptoRB = binding.cryptoRB;
        final RadioButton stockRB = binding.stockRB;
        final RadioGroup valueType = binding.radioGroup;

        valueType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.cryptoRB){
                    Data data = new Data.Builder()
                            .putString("symbol_type", "crypto")
                            .build();

                    OneTimeWorkRequest symbolInfoWork =
                            new OneTimeWorkRequest.Builder(SymbolnfoWorker.class)
                                    .setInputData(data)
                                    .build();


                    WorkManager.getInstance().getWorkInfoByIdLiveData(symbolInfoWork.getId())
                            .observe((LifecycleOwner) getContext(), status -> {
                                if (status != null && status.getState().isFinished() && status.getOutputData().getString("value")!="Error") {
                                    System.out.println(status.getOutputData().getString("value"));
                                    String queryStatus = status.getOutputData().getString("value").split("Symbol")[1].replace("\\", "").replace("}", "").replaceFirst("\":", "");
                                    queryStatus += "}";
                                    JSONParser parser = new JSONParser();
                                    try {
                                        symbolJSON = (JSONObject) parser.parse(queryStatus.toString());
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    String [] values= queryStatus.replace("{", "").replace("}","").split("\",");
                                    try {
                                        List<String> names = new ArrayList<String>();
                                        for (String retreivedValue : values) {
                                            names.add(retreivedValue.split(":")[0].replace("\"", ""));
                                        }
                                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, names);
                                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        symbol.setAdapter(dataAdapter);

                                    } catch (NullPointerException e) {
                                        System.out.println("NO DATA");
                                    }
                                }
                            });

                    WorkManager.getInstance().enqueue(symbolInfoWork);
                } else if (checkedId==R.id.stockRB) {
                    Data data = new Data.Builder()
                            .putString("symbol_type", "stock")
                            .build();

                    OneTimeWorkRequest symbolInfoWork =
                            new OneTimeWorkRequest.Builder(SymbolnfoWorker.class)
                                    .setInputData(data)
                                    .build();


                    WorkManager.getInstance().getWorkInfoByIdLiveData(symbolInfoWork.getId())
                            .observe((LifecycleOwner) getContext(), status -> {
                                if (status != null && status.getState().isFinished() && status.getOutputData().getString("value")!="Error") {
                                    System.out.println(status.getOutputData().getString("value"));
                                    String queryStatus = status.getOutputData().getString("value").split("Symbol")[1].replace("\\", "").replace("}", "").replaceFirst("\":", "");
                                    queryStatus += "}";
                                    JSONParser parser = new JSONParser();
                                    try {
                                        symbolJSON = (JSONObject) parser.parse(queryStatus.toString());
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    String [] values= queryStatus.replace("{", "").replace("}","").split("\",");
                                    try {
                                        List<String> names = new ArrayList<String>();
                                        for (String retreivedValue : values) {
                                            names.add(retreivedValue.split(":")[0].replace("\"", ""));
                                        }
                                        Collections.sort(names);
                                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, names);
                                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        symbol.setAdapter(dataAdapter);

                                    } catch (NullPointerException e) {
                                        System.out.println("NO DATA");
                                    }
                                }
                            });

                    WorkManager.getInstance().enqueue(symbolInfoWork);
                }
            }
        });

        addstockbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View v) {
                String username = MainActivity.username;
                if (username != null) {
                    System.out.println(symbol.getSelectedItem().toString());
                    System.out.println(symbolJSON.get(symbol.getSelectedItem().toString()));
                    Data data = new Data.Builder()
                            .putString("symbol", String.valueOf(symbolJSON.get(symbol.getSelectedItem().toString())))
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