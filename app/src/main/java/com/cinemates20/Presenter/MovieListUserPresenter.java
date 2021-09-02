package com.cinemates20.Presenter;

import com.cinemates20.DAO.Implements.MovieListDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.MovieListDAO;
import com.cinemates20.View.MovieListUserFragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieListUserPresenter {

    private MovieListUserFragment movieListUserFragment;

    public MovieListUserPresenter (MovieListUserFragment movieListUserFragment){
        this.movieListUserFragment = movieListUserFragment;
    }

    public void seeMovieList() {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        String clickedList = movieListUserFragment.getClickedList();

        MovieListDAO movieListDAO = new MovieListDAO_Firestore(movieListUserFragment.getContext());
        List<Integer> idMoviesList = movieListDAO.getMoviesByList(clickedList, currentUser);

        List<MovieDb> movieDbList = getMoviesOfList(idMoviesList);

        movieListUserFragment.setRecycler(movieDbList);
    }

    public List<MovieDb> getMoviesOfList(List<Integer> idMoviesList) {
        List<MovieDb> movieDbList = new ArrayList<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(() -> {
            TmdbApi api = new TmdbApi("27d6d704f8c045e37c749748d75b3f46");

            for (int i = 0; i < idMoviesList.size(); i++)
                movieDbList.add(api.getMovies()
                        .getMovie(idMoviesList.get(i), "en"));

        });

        //Waits for the computation to complete, and then retrieves its result.
        try {
            future.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        return movieDbList;
    }

    public void removeMovieFromList(String idMovie, String clickedList) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        MovieListDAO movieListDAO = new MovieListDAO_Firestore(movieListUserFragment.getContext());

        movieListDAO.removeMovieFromList(idMovie, clickedList, currentUser);
    }
}
