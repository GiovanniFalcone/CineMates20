package com.cinemates20.Presenter;

import android.os.AsyncTask;
import android.os.Bundle;

import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.GenresFragment;
import com.cinemates20.View.SearchMovieTabFragment;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.MovieDb;

public class SearchMoviePresenter extends AsyncTask<String, Void, List<MovieDb>> {

    public interface SearchMoviesCallback{
        void onResults(List<MovieDb> movieDbList);
    }

    private SearchMoviesCallback searchMoviesCallback;
    private final SearchMovieTabFragment searchMovieTabFragment;

    public SearchMoviePresenter(SearchMovieTabFragment searchMovieTabFragment) {
        this.searchMovieTabFragment = searchMovieTabFragment;
    }

    public void setSearchMoviesCallback(SearchMoviesCallback searchMoviesCallback){
        this.searchMoviesCallback = searchMoviesCallback;
    }

    @Override
    protected List<MovieDb> doInBackground(String... movies) {

        List<MovieDb> movieList = new ArrayList<>();

        TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");
        TmdbSearch ricerca = api.getSearch();
        String movie = movies[0];

        if (!movie.isEmpty())
            movieList = ricerca.searchMovie(movie, 0, "", true, 0).getResults();

        return movieList;
    }

    @Override
    protected void onPostExecute(List<MovieDb> results) {
        searchMoviesCallback.onResults(results);
    }

    public void onGenreClicked(int idGenre, String genreName) {
        GenresFragment genresFragment = new GenresFragment();
        Bundle args = new Bundle();
        args.putInt("idGenre", idGenre);
        args.putString("genreName", genreName);
        genresFragment.setArguments(args);
        Utils.changeFragment(searchMovieTabFragment, genresFragment, R.id.nav_host_fragment_activity_main);
    }
}

