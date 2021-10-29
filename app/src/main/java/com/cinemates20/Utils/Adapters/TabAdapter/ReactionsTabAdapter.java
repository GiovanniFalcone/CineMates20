package com.cinemates20.Utils.Adapters.TabAdapter;

import android.content.Context;
import android.os.Bundle;

import com.cinemates20.Model.User;
import com.cinemates20.View.UsersReactionsFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReactionsTabAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final int totalTabs;
    private final String idReview;
    private final User user;
    private final List<String> sentList, receivedList;


    public ReactionsTabAdapter(FragmentManager fragmentManager, Context context, int totalTabs, String idReview, Serializable user, ArrayList<String> sentList, ArrayList<String> receivedList) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.totalTabs = totalTabs;
        this.idReview = idReview;
        this.user = (User) user;
        this.sentList = sentList;
        this.receivedList = receivedList;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        UsersReactionsFragment usersReactionsFragment = new UsersReactionsFragment();
        Bundle args = new Bundle();
        args.putString("idReview", idReview);
        args.putInt("index", position);
        args.putSerializable("User", (Serializable) user);
        args.putStringArrayList("sentList", (ArrayList<String>) sentList);
        args.putStringArrayList("receivedList", (ArrayList<String>) receivedList);
        usersReactionsFragment.setArguments(args);
        return usersReactionsFragment;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
