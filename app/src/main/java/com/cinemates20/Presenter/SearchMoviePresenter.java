package com.cinemates20.Presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Implements.MovieDAO_TMDB;
import com.cinemates20.Model.DAO.Interface.Callbacks.MovieCallback;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.Model.Movie;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.GenresFragment;
import com.cinemates20.View.MovieCardFragment;
import com.cinemates20.View.SearchMovieTabFragment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.movito.themoviedbapi.model.MovieDb;

public class SearchMoviePresenter {

    private final SearchMovieTabFragment searchMovieTabFragment;

    public SearchMoviePresenter(SearchMovieTabFragment searchMovieTabFragment) {
        this.searchMovieTabFragment = searchMovieTabFragment;
    }

    public void onSearchMovie(String query) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
            List<Movie> movieDbList = movieDAO.getMovies(query);
            handler.post(() -> searchMovieTabFragment.setMovieRecycler(movieDbList));
        });
    }

    /**
     * Open new Fragment and pass it the id and name genre.
     * @param idGenre  the id of genre clicked
     * @param genreName  the name of genre clicked
     */
    public void onGenreClicked(int idGenre, String genreName) {
        GenresFragment genresFragment = new GenresFragment();
        Bundle args = new Bundle();
        args.putInt("idGenre", idGenre);
        args.putString("genreName", genreName);
        genresFragment.setArguments(args);

        changeFragment(genresFragment, R.id.nav_host_fragment_activity_main);
    }

    /**
     * Open the movie card of clicked movie.
     * @param filteredMovie the movie searched that user clicked
     */
    public void onClickMovie(Movie filteredMovie) {
        MovieCardFragment movieCardFragment = new MovieCardFragment();
        Fragment current = searchMovieTabFragment.requireActivity()
                .getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (!current.getClass().equals(movieCardFragment.getClass())) {
            Bundle args = new Bundle();
            args.putInt("MovieID", filteredMovie.getMovieDb().getId());
            args.putString("MovieTitle", filteredMovie.getMovieDb().getTitle());
            args.putString("MovieUrl", filteredMovie.getMovieDb().getPosterPath());
            args.putString("MovieOverview", filteredMovie.getMovieDb().getOverview());
            args.putFloat("MovieRating", filteredMovie.getMovieDb().getVoteAverage());
            movieCardFragment.setArguments(args);
            Utils.changeFragment_BottomAnim(searchMovieTabFragment, movieCardFragment, R.id.nav_host_fragment_activity_main);
        } else
            Toast.makeText(searchMovieTabFragment.getContext(), "Be patient, the card is loading", Toast.LENGTH_SHORT).show();
    }

    private void changeFragment(Fragment newFragment, int idLayout) {
        searchMovieTabFragment.requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.bottom_out  // popExit
                )
                .replace(R.id.nav_host_fragment_activity_main, newFragment)
                .addToBackStack(null)
                .commit();
    }
}

