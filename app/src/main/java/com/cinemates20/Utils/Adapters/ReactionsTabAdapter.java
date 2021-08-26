package com.cinemates20.Utils.Adapters;

import android.content.Context;
import android.os.Bundle;

import com.cinemates20.View.UsersReactionsFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ReactionsTabAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;
    String idReview;


    public ReactionsTabAdapter(FragmentManager fragmentManager, Context context, int totalTabs, String idReview) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.totalTabs = totalTabs;
        this.idReview = idReview;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        UsersReactionsFragment usersReactionsFragment = new UsersReactionsFragment();
        Bundle args = new Bundle();
        args.putString("idReview", idReview);
        args.putInt("index", position);
        usersReactionsFragment.setArguments(args);
        return usersReactionsFragment;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
