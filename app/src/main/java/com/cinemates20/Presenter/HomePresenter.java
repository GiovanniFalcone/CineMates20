package com.cinemates20.Presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cinemates20.Model.DAO.Implements.MovieDAO_TMDB;
import com.cinemates20.Model.DAO.Interface.Callbacks.MovieCallback;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.HomeFragment;
import com.cinemates20.View.MovieCardFragment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import info.movito.themoviedbapi.model.MovieDb;

public class HomePresenter{

    private final HomeFragment homeFragment;

    public HomePresenter(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    /**
     * This method will get the list of popular, top rated and now playing movie and will set
     * the lists into home.
     */
    public void setHome() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = new MovieDAO_TMDB();
            AtomicInteger idMovie_nowPlaying = new AtomicInteger();

            //Set popularMovie
            movieDAO.getPopular(new MovieCallback() {
                @Override
                public void setMovie(List<MovieDb> movieDbList) {
                    handler.post(() -> homeFragment.setFeaturedMovie(movieDbList));
                }
            });

            //Set topRatedMovie
            movieDAO.getTopRated(new MovieCallback() {
                @Override
                public void setMovie(List<MovieDb> movieDbList) {
                    handler.post(() -> homeFragment.setRecycler(movieDbList));
                }
            });

            //Set featured movie
            movieDAO.getNowPlaying(new MovieCallback() {
                @Override
                public void setMovie(List<MovieDb> movieDbList) {
                    int randomNum = (int) ((Math.random() * (20 - 1)) + 1);
                    MovieDb movieDb = movieDbList.get(randomNum);
                    idMovie_nowPlaying.set(movieDb.getId());

                    handler.post(() ->
                            homeFragment.setRandomMovie(movieDb));
                }
            });

            movieDAO.getLogo(idMovie_nowPlaying.get(), new MovieCallback() {
                @Override
                public void setLogo(String pathLogo) {
                    homeFragment.setLogoMovie(pathLogo);
                }
            });
        });
    }

    /**
     * Open the movie card of clicked movie
     * @param movieClicked the movie clicked by user
     */
    public void onClickMovie(MovieDb movieClicked) {
        MovieCardFragment movieCardFragment = new MovieCardFragment();
        Fragment current = homeFragment.requireActivity()
                .getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (!current.getClass().equals(movieCardFragment.getClass())) {
            Bundle args = new Bundle();
            args.putInt("MovieID", movieClicked.getId());
            args.putString("MovieTitle", movieClicked.getTitle());
            args.putString("MovieUrl", movieClicked.getPosterPath());
            args.putString("MovieImg", movieClicked.getPosterPath());
            args.putString("MovieOverview", movieClicked.getOverview());
            args.putFloat("MovieRating", movieClicked.getVoteAverage());
            args.putString("MoviePoster", movieClicked.getPosterPath());
            movieCardFragment.setArguments(args);
            Utils.changeFragment_BottomAnim(homeFragment, movieCardFragment, R.id.nav_host_fragment_activity_main);
        } else
            Toast.makeText(homeFragment.getContext(), "Be patient, the card is loading", Toast.LENGTH_SHORT).show();
    }
}
