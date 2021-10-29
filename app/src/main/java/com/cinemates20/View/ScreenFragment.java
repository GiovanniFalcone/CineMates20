package com.cinemates20.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinemates20.R;
import com.cinemates20.Utils.Adapters.ScreenAdapterPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

import info.movito.themoviedbapi.model.Artwork;


public class ScreenFragment extends Fragment {

    public static ScreenFragment newInstance(int currentItem, List<Artwork> artworks) {
        ScreenFragment screenFragment = new ScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("initial_item", currentItem);
        bundle.putSerializable("artworks_item", (Serializable) artworks);
        screenFragment.setArguments(bundle);
        return screenFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setSharedElementReturnTransition(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_screen, container, false);

        FloatingActionButton button = view.findViewById(R.id.backButton);
        button.setOnClickListener(view1 -> requireActivity().onBackPressed());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int currentItem = requireArguments().getInt("initial_item");
        List<Artwork> artworks = (List<Artwork>) requireArguments().getSerializable("artworks_item");

        ScreenAdapterPager screenAdapterPager = new ScreenAdapterPager(getChildFragmentManager(), artworks);

        ViewPager viewPager = view.findViewById(R.id.screen_viewPager);
        viewPager.setAdapter(screenAdapterPager);
        viewPager.setCurrentItem(currentItem);
    }
}