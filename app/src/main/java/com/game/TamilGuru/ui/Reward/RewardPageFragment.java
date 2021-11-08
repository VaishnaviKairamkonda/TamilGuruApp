package com.game.TamilGuru.ui.Reward;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.game.TamilGuru.R;
import com.game.TamilGuru.databinding.FragmentRewardPageBinding;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class RewardPageFragment extends Fragment {

    private RewardPageViewModel mViewModel;
    FragmentRewardPageBinding fragmentRewardPageBinding;
    private KonfettiView celebrationView;

    public static RewardPageFragment newInstance() {
        return new RewardPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reward_page, container, false);
        fragmentRewardPageBinding = FragmentRewardPageBinding.bind(v);
        return fragmentRewardPageBinding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        celebrationView = view.findViewById(R.id.celebrationView);
        celebrationView.build()
                .addColors(Color.RED,Color.YELLOW,Color.BLUE,Color.GREEN, Color.MAGENTA)
                .setDirection(0.0,359.0)
                .setSpeed(1f,5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(6000L)
                .addShapes(Shape.CIRCLE,Shape.RECT)
                .addSizes(new Size(12,5))
                .setPosition(-50f,celebrationView.getWidth()+50f,-50f,-50f)
                .streamFor(500,10000L);

        fragmentRewardPageBinding.rewardpagelayout.setOnClickListener(this::launchHomePage);

    }
    private void launchHomePage(View v){
        NavDirections toHomeFragment = RewardPageFragmentDirections.actionRewardPageFragmentToRewardProfilePageFragment();

        Navigation.findNavController(fragmentRewardPageBinding.getRoot()).navigate(toHomeFragment);
    }



}