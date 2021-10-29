package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Implements.MovieDAO_TMDB;
import com.cinemates20.Model.DAO.Interface.Firestore.MovieListDAO;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;
import com.cinemates20.View.MovieListUserFragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieListUserPresenter {

    private final MovieListUserFragment movieListUserFragment;

    public MovieListUserPresenter (MovieListUserFragment movieListUserFragment){
        this.movieListUserFragment = movieListUserFragment;
    }

    public void seeMovieList() {
        String nameList = movieListUserFragment.getNameClickedList();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);

        MovieListDAO movieListDAO = daoFactory.getMovieListDAO();
        List<Integer> idMoviesList = movieListDAO.getMoviesByList(nameList, currentUser);

        MovieDAO movieDAO = new MovieDAO_TMDB();
        List<MovieDb> movieDbList = movieDAO.getMoviesOfList(idMoviesList);

        movieListUserFragment.setRecycler(movieDbList);
    }

    public void removeMovieFromList(String idMovie, String clickedList) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        MovieListDAO movieListDAO = daoFactory.getMovieListDAO();
        movieListDAO.removeMovieFromList(idMovie, clickedList, currentUser);
    }
}
