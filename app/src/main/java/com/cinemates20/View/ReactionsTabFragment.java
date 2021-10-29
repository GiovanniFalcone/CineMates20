package com.cinemates20.View;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.TabAdapter.ReactionsTabAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;


public class ReactionsTabFragment extends BottomSheetDialogFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reactions_total, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_thumb_up_24));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_thumb_down_24));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_heart));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_clapping));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_angry));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ReactionsTabAdapter reactionsTabAdapter =
                new ReactionsTabAdapter(getChildFragmentManager(),
                        requireContext(),
                        tabLayout.getTabCount(),
                        requireArguments().getString("idReview"),
                        requireArguments().getSerializable("User"),
                        requireArguments().getStringArrayList("sentList"),
                        requireArguments().getStringArrayList("receivedList"));

        viewPager.setAdapter(reactionsTabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setAlpha(0);
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        selectedListener();
        return view;
    }

    private void selectedListener() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}