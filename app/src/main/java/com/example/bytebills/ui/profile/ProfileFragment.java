package com.example.bytebills.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.bytebills.R;
import com.example.bytebills.controller.UserProfileWorker;
import com.example.bytebills.databinding.FragmentProfileBinding;
import com.example.bytebills.databinding.FragmentSlideshowBinding;
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewAssetCount;
    private TextView textViewAvgValue;
    private TextView textViewTotalValue;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewUsername = binding.tvUsername;
        textViewEmail = binding.tvEmail;
        textViewTotalValue = binding.tvTotalValue;

        OneTimeWorkRequest profileWork = new OneTimeWorkRequest.Builder(UserProfileWorker.class).build();
        WorkManager.getInstance(requireActivity()).getWorkInfoByIdLiveData(profileWork.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo != null && workInfo.getState().isFinished()) {
                workInfo.getOutputData().getString("username");
                workInfo.getOutputData().getString("email");
                workInfo.getOutputData().getInt("asset_count", 0);
                workInfo.getOutputData().getString("avg_value");
                workInfo.getOutputData().getString("total_value");
                workInfo.getOutputData().getString("most_common_asset");
                workInfo.getOutputData().getStringArray("top_3_assets");

                textViewUsername.setText(workInfo.getOutputData().getString("username"));
                textViewEmail.setText(workInfo.getOutputData().getString("email"));
                textViewTotalValue.setText(workInfo.getOutputData().getString("total_value"));

            }
        });
        WorkManager.getInstance(requireActivity()).enqueue(profileWork);

        return root;
    }
}