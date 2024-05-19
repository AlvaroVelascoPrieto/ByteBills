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
    private TextView textViewMostCommon;
    private TextView textViewTotalValue;
    private TextView textViewTop1Name;
    private TextView textViewTop2Name;
    private TextView textViewTop3Name;
    private TextView textViewTop1Value;
    private TextView textViewTop2Value;
    private TextView textViewTop3Value;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewUsername = binding.tvUsername;
        textViewEmail = binding.tvEmail;
        textViewTotalValue = binding.tvTotalValue;
        textViewAssetCount = binding.tvAssetCount;
        textViewMostCommon = binding.tvMostCommon;
        textViewTop1Name = binding.tvTop1Name;
        textViewTop2Name = binding.tvTop2Name;
        textViewTop3Name = binding.tvTop3Name;
        textViewTop1Value = binding.tvTop1Value;
        textViewTop2Value = binding.tvTop2Value;
        textViewTop3Value = binding.tvTop3Value;


        OneTimeWorkRequest profileWork = new OneTimeWorkRequest.Builder(UserProfileWorker.class).build();
        WorkManager.getInstance(requireActivity()).getWorkInfoByIdLiveData(profileWork.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo != null && workInfo.getState().isFinished()) {
                String username = workInfo.getOutputData().getString("username");
                String email = workInfo.getOutputData().getString("email");
                int assetCount = workInfo.getOutputData().getInt("asset_count", 0);
                String avgValue = workInfo.getOutputData().getString("avg_value");
                String totalValue = workInfo.getOutputData().getString("total_value");
                String mostCommonConglomerate = workInfo.getOutputData().getString("most_common_asset");
                String[] top3array = workInfo.getOutputData().getStringArray("top_3_assets");

                String mostCommonAssetName = mostCommonConglomerate.split(":")[0];
                String mostCommonAssetQty = mostCommonConglomerate.split(":")[1].split(",")[0];
                String mostCommonAssetValue = mostCommonConglomerate.split(":")[1].split(",")[1];

                String top1Name = top3array[0].split(":")[0];
                String top2Name = top3array[1].split(":")[0];
                String top3Name = top3array[2].split(":")[0];

                String top1Value = top3array[0].split(":")[1].split(",")[1];
                String top2Value = top3array[1].split(":")[1].split(",")[1];
                String top3Value = top3array[2].split(":")[1].split(",")[1];

                textViewUsername.setText(username);
                textViewEmail.setText(email);
                textViewTotalValue.setText(totalValue + "€");
                textViewMostCommon.setText(mostCommonAssetName);
                textViewAssetCount.setText(String.valueOf(assetCount));

                textViewTop1Name.setText(top1Name);
                textViewTop2Name.setText(top2Name);
                textViewTop3Name.setText(top3Name);
                textViewTop1Value.setText(top1Value + "€");
                textViewTop2Value.setText(top2Value + "€");
                textViewTop3Value.setText(top3Value + "€");

            }
        });
        WorkManager.getInstance(requireActivity()).enqueue(profileWork);

        return root;
    }
}