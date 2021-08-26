package com.cinemates20.Presenter;

import android.os.AsyncTask;
import android.os.Bundle;


import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.HomeFragment;
import com.cinemates20.View.MovieCardFragment;

import org.apache.commons.lang3.tuple.Triple;
import java.util.List;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class HomePresenter extends AsyncTask<Void, Void, Triple<List<MovieDb>, List<MovieDb>, List<MovieDb>>> {

    public interface HomeCallback{
        void setMoviesHome(Triple<List<MovieDb>, List<MovieDb>, List<MovieDb>> ret);
    }

    private final HomeFragment homeFragment;
    private HomeCallback homeCallback;

    public HomePresenter(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public void setHomeFragment(HomeCallback homeCallback){
        this.homeCallback = homeCallback;
    }

    @Override
    protected Triple<List<MovieDb>, List<MovieDb>, List<MovieDb>> doInBackground(Void... voids) {
        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbMovies movies = api.getMovies();
        List<MovieDb> popular = movies.getPopularMovies(null, 0).getResults();
        List<MovieDb> nowPlaying = movies.getNowPlayingMovies(null, 0, null).getResults();
        List<MovieDb> topRated = movies.getTopRatedMovies(null, 0).getResults();

        return new Triple<List<MovieDb>, List<MovieDb>, List<MovieDb>>() {
            @Override
            public List<MovieDb> getLeft() {
                return popular;
            }

            @Override
            public List<MovieDb> getMiddle() {
                return nowPlaying;
            }

            @Override
            public List<MovieDb> getRight() {
                return topRated;
            }
        };
    }

    @Override
    protected void onPostExecute(Triple<List<MovieDb>, List<MovieDb>, List<MovieDb>> ret) {
        homeCallback.setMoviesHome(ret);
    }

    /**
     * Open the movie card of clicked movie
     * @param movieDbList - the list of movies
     * @param position - the position of clicked movie
     */
    public void onClickMovie(List<MovieDb> movieDbList, int position) {
        MovieCardFragment movieCardFragment = new MovieCardFragment();
        Bundle args = new Bundle();
        args.putInt("MovieID", movieDbList.get(position).getId());
        args.putString("MovieTitle", movieDbList.get(position).getTitle());
        args.putString("MovieUrl", movieDbList.get(position).getPosterPath());
        args.putString("MovieOverview", movieDbList.get(position).getOverview());
        args.putFloat("MovieRating", movieDbList.get(position).getVoteAverage()/2);
        movieCardFragment.setArguments(args);
        Utils.changeFragment(homeFragment, movieCardFragment, R.id.nav_host_fragment_activity_main);
    }
}
