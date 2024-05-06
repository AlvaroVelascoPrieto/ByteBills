package com.example.bytebills.ui.registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.MainActivity;
import com.example.bytebills.controller.RegisterWorker;
import com.example.bytebills.databinding.FragmentRegistrationBinding;

public class  RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView usernameTV = binding.username;
        final TextView emailTV = binding.email;
        final TextView passwordTV = binding.password;
        final TextView password2TV = binding.password2;
        final Button register = binding.registerbtn;

        register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View v) {
                String username = usernameTV.getText().toString();
                String email = emailTV.getText().toString();
                String password = passwordTV.getText().toString();
                String password2 = password2TV.getText().toString();
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter all the data...", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(password2)){
                    Toast.makeText(getActivity(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }else{ //Peticion asincrona al servidor remoto con la base de datos

                    Data data = new Data.Builder()
                            .putString("username", username)
                            .putString("email", email)
                            .putString("password", password)
                            .build();

                    OneTimeWorkRequest registerWork =
                            new OneTimeWorkRequest.Builder(RegisterWorker.class)
                                    .setInputData(data)
                                    .build();

                    WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(registerWork.getId())
                        .observe(RegistrationFragment.this, status -> {
                            if (status != null && status.getState().isFinished()) {
                                String registerStatus = status.getOutputData().getString("status");
                                try {
                                    if (registerStatus.equals("Ok")) {
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        i.putExtra("username", username);
                                        startActivity(i);
                                    }
                                } catch (NullPointerException e) {
                                    Toast.makeText(getActivity(), "An unexpected error ocurred, try again later", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    WorkManager.getInstance(requireContext()).enqueue(registerWork);
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