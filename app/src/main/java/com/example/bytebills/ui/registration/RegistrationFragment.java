package com.example.bytebills.ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.controller.RemoteDBHandler;
import com.example.bytebills.databinding.FragmentRegistrationBinding;

public class  RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegistrationViewModel homeViewModel =
                new ViewModelProvider(this).get(RegistrationViewModel.class);

        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView usernameTV = binding.username;
        final TextView emailTV = binding.email;
        final TextView passwordTV = binding.password;
        final TextView password2TV = binding.password2;
        final Button register = binding.registerbtn;

        register.setOnClickListener(new View.OnClickListener() {
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
                            .putString("tag", "Register")
                            .putString("username", username)
                            .putString("email", email)
                            .putString("password", password)
                            .build();

                    OneTimeWorkRequest registerWork =
                            new OneTimeWorkRequest.Builder(RemoteDBHandler.class)
                                    .setInputData(data)
                                    .build();

                    //TODO: Dar contexto
                    WorkManager.getInstance().enqueue(registerWork);
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