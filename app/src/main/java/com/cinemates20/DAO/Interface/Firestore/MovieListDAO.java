package com.cinemates20.DAO.Interface.Firestore;

import java.util.List;

public interface MovieListDAO {
    void addMovieToList(String currentUser, String listName, String idMovie);



    List<Integer> getMoviesByList(String clickedList, String currentUser);



    List<String> getListsThatContainsCurrentMovie(String idMovie, String currentUser);

    void removeMovieFromList(String movieTitle, String listName, String currentUser);
}
