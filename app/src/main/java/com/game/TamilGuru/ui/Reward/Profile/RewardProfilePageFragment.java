package com.game.TamilGuru.ui.Reward.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.game.TamilGuru.R;


public class RewardProfilePageFragment extends Fragment {

    private RewardProfilePageViewModel mViewModel;

    public static RewardProfilePageFragment newInstance() {
        return new RewardProfilePageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reward_profile_page_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RewardProfilePageViewModel.class);
        // TODO: Use the ViewModel
    }

}