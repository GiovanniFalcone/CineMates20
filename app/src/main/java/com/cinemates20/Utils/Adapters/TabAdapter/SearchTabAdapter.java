package com.cinemates20.Utils.Adapters.TabAdapter;

import android.content.Context;

import com.cinemates20.View.SearchMovieTabFragment;
import com.cinemates20.View.SearchUserTabFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SearchTabAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    @Override
    public int getCount() {
        return totalTabs;
    }

    public SearchTabAdapter(FragmentManager fragmentManager, Context context, int totalTabs) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    public Fragment getItem(int position){
        switch (position){
            case 0:
                return new SearchMovieTabFragment();
            case 1:
                return new SearchUserTabFragment();
            default:
                return null;
        }
    }
}
