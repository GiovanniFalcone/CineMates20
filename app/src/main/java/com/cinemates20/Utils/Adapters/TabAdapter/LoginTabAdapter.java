package com.cinemates20.Utils.Adapters.TabAdapter;

import android.content.Context;

import com.cinemates20.View.LoginTabFragment;
import com.cinemates20.View.SignupTabFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginTabAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    @Override
    public int getCount() {
        return totalTabs;
    }

    public LoginTabAdapter(FragmentManager fragmentManager, Context context, int totalTabs) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new LoginTabFragment();
            case 1:
                return new SignupTabFragment();
            default:
                return null;
        }
    }
}
