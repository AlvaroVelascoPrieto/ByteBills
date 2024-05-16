package com.example.bytebills.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.MainActivity;
import com.example.bytebills.R;
import com.example.bytebills.controller.StockInfoWorker;
import com.example.bytebills.controller.StocksUserWorker;
import com.example.bytebills.databinding.FragmentHomeBinding;
import com.example.bytebills.model.BillGroup;
import com.example.bytebills.ui.addStock.AddStockFragment;
import com.example.bytebills.ui.registration.RegistrationFragment;
import com.example.bytebills.ui.stockvalue.StockValueActivity;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private StockAdapter adapter;
    private List<BillGroup> billGroupList;
    private FragmentHomeBinding binding;
    private String TAG = "HomeFragment";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Initialize RecyclerView
        recyclerView = binding.recyclerViewBillGroups;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        billGroupList = new ArrayList<BillGroup>();
        String username = MainActivity.username;

        Data data = new Data.Builder()
                .putString("username", username)
                .build();

        OneTimeWorkRequest stocksUserWork =
                new OneTimeWorkRequest.Builder(StocksUserWorker.class)
                        .setInputData(data)
                        .build();


        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(stocksUserWork.getId())
                .observe(getViewLifecycleOwner(), status -> {
                    if (status != null && status.getState().isFinished()) {
                        String[] stocks = status.getOutputData().getStringArray("stocks");
                        for (int i = 0; i < stocks.length; i = i + 2) {
                            Log.d(TAG, stocks[i] + "   " + stocks[i + 1]);
                            String title = stocks[i];
                            String description = stocks[i + 1];
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                billGroupList.add(new BillGroup(0, title, description, LocalDateTime.now().toString()));
                            }

                            // Set up RecyclerView adapter
                            adapter = new StockAdapter(billGroupList);
                            recyclerView.setAdapter(adapter);

                            recyclerView.startAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.scroll_animation));
                        }

                    }
                });

        WorkManager.getInstance(requireContext()).enqueue(stocksUserWork);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddStockFragment newAddStockFragment = new AddStockFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, newAddStockFragment);
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