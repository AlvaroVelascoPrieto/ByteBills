package com.example.bytebills.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bytebills.databinding.BillGroupCardBinding;
import com.example.bytebills.databinding.FragmentHomeBinding;
import com.example.bytebills.model.BillGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private BillGroupAdapter adapter;
    private List<BillGroup> billGroupList;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Initialize RecyclerView
        recyclerView = binding.recyclerViewBillGroups;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        // Fetch tasks from database
        billGroupList = new ArrayList<BillGroup>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            billGroupList.add(new BillGroup(0, "BILL GROUP TITLE", "Bill Group Description", LocalDateTime.now().toString()));
        }

        // Set up RecyclerView adapter
        adapter = new BillGroupAdapter(billGroupList);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}