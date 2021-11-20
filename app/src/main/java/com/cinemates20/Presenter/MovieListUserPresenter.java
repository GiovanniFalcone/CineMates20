package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Implements.MovieDAO_TMDB;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.MovieListDAO;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.MovieListUserFragment;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieListUserPresenter {

    private final MovieListUserFragment movieListUserFragment;

    public MovieListUserPresenter (MovieListUserFragment movieListUserFragment){
        this.movieListUserFragment = movieListUserFragment;
    }

    public void seeMovieList(String nameListClicked) {
        String currentUser = User.getCurrentUser();

        MovieListDAO movieListDAO = DAOFactory.getMovieListDAO(DAOFactory.FIREBASE);
        List<Integer> idMoviesList = movieListDAO.getMoviesByList(nameListClicked, currentUser);

        MovieDAO movieDAO = new MovieDAO_TMDB();
        List<MovieDb> movieDbList = movieDAO.getMoviesOfList(idMoviesList);

        movieListUserFragment.setRecycler(movieDbList);
    }

    public void removeMovieFromList(String idMovie, String clickedList) {
        String currentUser = User.getCurrentUser();

        MovieListDAO movieListDAO = DAOFactory.getMovieListDAO(DAOFactory.FIREBASE);
        movieListDAO.removeMovieFromList(idMovie, clickedList, currentUser);
    }
}
