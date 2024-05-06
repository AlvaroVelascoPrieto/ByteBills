package com.example.bytebills.ui.home;

import android.os.Build;
import android.os.Bundle;
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

import com.example.bytebills.R;
import com.example.bytebills.databinding.FragmentHomeBinding;
import com.example.bytebills.model.BillGroup;
import com.example.bytebills.ui.addStock.AddStockFragment;
import com.example.bytebills.ui.registration.RegistrationFragment;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private StockAdapter adapter;
    private List<BillGroup> billGroupList;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Initialize RecyclerView
        recyclerView = binding.recyclerViewBillGroups;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO: Fetch tasks from DB
        billGroupList = new ArrayList<BillGroup>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            billGroupList.add(new BillGroup(0, "TESLA", "TSLA", LocalDateTime.now().toString()));
        }

        // Set up RecyclerView adapter
        adapter = new StockAdapter(billGroupList);
        recyclerView.setAdapter(adapter);

        recyclerView.startAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.scroll_animation));

        binding.resultCard.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddStockFragment newAddStockFragment = new AddStockFragment();

                //TODO: Set real username taken from where this fragment is made.
                Bundle bundle = new Bundle();
                bundle.putString("username", "admin");
                newAddStockFragment.setArguments(bundle);

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