package com.example.bytebills.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SharedMemory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.Identification;
import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.controller.LoginWorker;
import com.example.bytebills.controller.RegisterWorker;
import com.example.bytebills.databinding.FragmentHomeBinding;
import com.example.bytebills.databinding.FragmentLoginBinding;
import com.example.bytebills.ui.registration.RegistrationFragment;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView username = binding.username;
        final TextView password = binding.password;
        final Button loginbtn = binding.loginbtn;
        final Button signupbtn = binding.signupbtn;

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("FragmentLiveDataObserve")
            @Override
            public void onClick(View v) {
                //TODO: Check user credentials in DB
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                if(usernameStr.equals("admin") && passwordStr.equals("admin")){
                    //ADMIN
                    MainActivity.username = "admin";

                    Intent i = new Intent(getActivity(), MainActivity.class);
                    i.putExtra("username", username.getText().toString());
                    startActivity(i);
                } else {
                    //Check real

                    Data data = new Data.Builder()
                            .putString("username", usernameStr)
                            .putString("password", passwordStr)
                            .build();

                    OneTimeWorkRequest loginWork =
                            new OneTimeWorkRequest.Builder(LoginWorker.class)
                                    .setInputData(data)
                                    .build();

                    WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(loginWork.getId())
                            .observe(LoginFragment.this, status -> {
                                if (status != null && status.getState().isFinished()) {
                                    String loginStatus = status.getOutputData().getString("status");
                                    try {
                                        if (loginStatus.equals("Ok")) { //El inicio de sesion es correcto
                                            MainActivity.username = usernameStr;

                                            Intent i = new Intent(getActivity(), MainActivity.class);
                                            i.putExtra("username", username.getText().toString());
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(getActivity(), "Username or password are incorrect", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), "Network error, try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    WorkManager.getInstance(requireContext()).enqueue(loginWork);
                }

            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationFragment newRegistrationFragment = new RegistrationFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_indentification, newRegistrationFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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