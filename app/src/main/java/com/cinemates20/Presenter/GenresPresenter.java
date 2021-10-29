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
import com.cinemates20.Utils.Adapters.MovieAdapter;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.GenresFragment;
import com.cinemates20.View.MovieCardFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.movito.themoviedbapi.model.MovieDb;

public class GenresPresenter {

    private final GenresFragment genresFragment;
    private List<MovieDb> newMovieList = new ArrayList<>();

    public GenresPresenter(GenresFragment genresFragment) {
        this.genresFragment  = genresFragment;
    }

    public void onGenreClicked(int page, MovieAdapter movieAdapter) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = new MovieDAO_TMDB();
            movieDAO.getListMovieByGenre(genresFragment.idGenre(), page, new MovieCallback() {
                @Override
                public void setMovie(List<MovieDb> movieDbList) {
                    handler.post(() -> {
                        if(movieDbList != null)
                            newMovieList.addAll(movieDbList);
                        if(page  == 1)
                            genresFragment.setRecycler(newMovieList);
                        else
                            movieAdapter.notifyDataSetChanged();
                    });
                }
            });
        });
    }

    /**
     * Open the movie card of clicked movie
     * @param movieDb the movie clicked by user
     */
    public void onMovieClicked(MovieDb movieDb) {
        MovieCardFragment movieCardFragment = new MovieCardFragment();
        Fragment current = genresFragment.requireActivity()
                .getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (!current.getClass().equals(movieCardFragment.getClass())) {
            Bundle args = new Bundle();
            args.putInt("MovieID", movieDb.getId());
            args.putString("MovieTitle", movieDb.getTitle());
            args.putString("MovieUrl", movieDb.getPosterPath());
            args.putString("MovieOverview", movieDb.getOverview());
            args.putFloat("MovieRating", movieDb.getVoteAverage() / 2);
            movieCardFragment.setArguments(args);
            Utils.changeFragment_BottomAnim(genresFragment, movieCardFragment, R.id.nav_host_fragment_activity_main);
        } else
            Toast.makeText(genresFragment.getContext(), "Be patient, the card is loading", Toast.LENGTH_SHORT).show();
    }
}
