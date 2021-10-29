package com.cinemates20.Utils.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cinemates20.View.ScreenDetailFragment;

import java.util.List;

import info.movito.themoviedbapi.model.Artwork;

public class ScreenAdapterPager extends FragmentStatePagerAdapter {

    private List<Artwork> artworkList;

    public ScreenAdapterPager(FragmentManager fragmentManager, List<Artwork> artworks) {
        super(fragmentManager);
        this.artworkList = artworks;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Artwork artwork = artworkList.get(position);
        return ScreenDetailFragment.newInstance(artwork, "transitionScreen");
    }

    @Override
    public int getCount() {
        return artworkList.size();
    }
}
