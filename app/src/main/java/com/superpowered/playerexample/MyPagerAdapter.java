package com.superpowered.playerexample;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 2; // Adjust based on the number of screens

    public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MyPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MyPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return Play_fragment.newInstance();
            case 1:
                return Recycler_fragment.newInstance();
            default:
                return new Fragment(); // Return a default Fragment or handle the case

        }


    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }

    public Fragment getFragment(int position) {
        return createFragment(position);
    }


}