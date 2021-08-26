package com.cinemates20.Presenter;

import android.os.Bundle;

import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.GenresFragment;
import com.cinemates20.View.MovieCardFragment;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;

public class GenresPresenter {

    private GenresFragment genresFragment;

    public GenresPresenter(GenresFragment genresFragment) {
        this.genresFragment  = genresFragment;
    }


    public void onGenreClicked() {
        AtomicReference<List<MovieDb>> movies = new AtomicReference<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> {
            TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
            movies.set(api.getGenre()
                    .getGenreMovies(genresFragment.idGenre(), "en", 0, Boolean.TRUE)
                    .getResults());
        });

        try {
            future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        genresFragment.setRecycler(movies.get());
    }

    /**
     * Open the movie card of clicked movie
     * @param movieDbList - the list of movies
     * @param position - the position of clicked movie
     */
    public void onMovieClicked(List<MovieDb> movieDbList, int position) {
        MovieCardFragment movieCardFragment = new MovieCardFragment();
        Bundle args = new Bundle();
        args.putInt("MovieID", movieDbList.get(position).getId());
        args.putString("MovieTitle", movieDbList.get(position).getTitle());
        args.putString("MovieUrl", movieDbList.get(position).getPosterPath());
        args.putString("MovieOverview", movieDbList.get(position).getOverview());
        args.putFloat("MovieRating", movieDbList.get(position).getVoteAverage()/2);
        movieCardFragment.setArguments(args);
        Utils.changeFragment(genresFragment, movieCardFragment, R.id.nav_host_fragment_activity_main);
    }
}
