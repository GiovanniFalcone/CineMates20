package com.cinemates20.Presenter;

import android.os.Handler;
import android.os.Looper;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.MovieListDAO;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.Model.Movie;
import com.cinemates20.Model.User;
import com.cinemates20.View.MovieListUserFragment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieListUserPresenter {

    private final MovieListUserFragment movieListUserFragment;

    public MovieListUserPresenter (MovieListUserFragment movieListUserFragment){
        this.movieListUserFragment = movieListUserFragment;
    }

    public void seeMovieList(List<Integer> listIDMovie) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
            List<Movie> movieDbList = movieDAO.getMoviesOfList(listIDMovie);

            handler.post(() -> movieListUserFragment.setRecycler(movieDbList));
        });
    }

    public void removeMovieFromList(String idMovie, String clickedList) {
        String currentUser = User.getCurrentUser();

        MovieListDAO movieListDAO = DAOFactory.getMovieListDAO(DAOFactory.FIREBASE);
        movieListDAO.removeMovieFromList(idMovie, clickedList, currentUser);
    }
}
