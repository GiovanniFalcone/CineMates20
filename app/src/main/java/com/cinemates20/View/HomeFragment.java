package com.cinemates20.View;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import info.movito.themoviedbapi.model.MovieDb;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cinemates20.Utils.Adapters.MovieAdapter;
import com.cinemates20.Presenter.HomePresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.DepthPageTransformer;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;

public class HomeFragment extends Fragment {

    private MovieAdapter adapterMovie;
    private RecyclerView topRated;
    private HomePresenter homePresenter;
    private KenBurnsView posterRandomMovie;
    private ViewPager2 mostPopular;
    private ImageView logoMovie;
    private Button goToMovie;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mostPopular = view.findViewById(R.id.mostPopularViewPager);
        topRated = view.findViewById(R.id.topRatedRecyclerView);
        posterRandomMovie = view.findViewById(R.id.randomMovie);
        logoMovie = view.findViewById(R.id.logoMovie);
        goToMovie = view.findViewById(R.id.goToMovieButton);

        homePresenter = new HomePresenter(this);
        homePresenter.setHome();

        return view;
    }

    public void setFeaturedMovie(List<MovieDb> movieDbList){
        adapterMovie = new MovieAdapter(movieDbList, getContext(), 0);

        mostPopular.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mostPopular.setAdapter(adapterMovie);
        mostPopular.setOffscreenPageLimit(3);
        mostPopular.setPageTransformer(new DepthPageTransformer());
        clickListener(adapterMovie);
    }

    public void setRecycler(List<MovieDb> movieDbList){
        adapterMovie = new MovieAdapter(movieDbList, getContext(), 1);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topRated.setLayoutManager(horizontalLayoutManager);
        topRated.setAdapter(adapterMovie);
        clickListener(adapterMovie);
    }

    public void clickListener(MovieAdapter adapterMovie){
        adapterMovie.setOnItemClickListener(new MovieAdapter.ClickListener() {
            @Override
            public void onItemClickListener(MovieDb movieClicked) {
                homePresenter.onClickMovie(movieClicked);
            }
        });
    }

    public void setRandomMovie(MovieDb movieDb){
        if(isAdded()) {
            Glide.with(requireContext())
                    .asBitmap()
                    .load("http://image.tmdb.org/t/p/original" + movieDb.getBackdropPath())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            posterRandomMovie.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

            posterRandomMovie.setOnClickListener(view -> homePresenter.onClickMovie(movieDb));
            goToMovie.setOnClickListener(view -> homePresenter.onClickMovie(movieDb));
        }
    }

    public void setLogoMovie(String url){
        if(isAdded()) {
            requireActivity().runOnUiThread(() -> Glide.with(requireContext())
                    .asBitmap()
                    .load(url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            logoMovie.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    }));
        }
    }
}


