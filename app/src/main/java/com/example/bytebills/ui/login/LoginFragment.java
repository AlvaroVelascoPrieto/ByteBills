package com.example.bytebills.ui.login;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.bytebills.Identification;
import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
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
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    //correct
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    i.putExtra("username", username.getText().toString());
                    startActivity(i);
                }else
                    //incorrect
                    Toast.makeText(getActivity(),"User or password incorrect",Toast.LENGTH_SHORT).show();
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