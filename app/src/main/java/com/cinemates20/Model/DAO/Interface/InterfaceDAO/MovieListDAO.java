package com.cinemates20.Model.DAO.Interface.InterfaceDAO;

import com.cinemates20.Model.MovieList;

import java.util.List;

public interface MovieListDAO {
    void addCustomList(String nameList, String description, String currentUser);

    void addMovieToList(String currentUser, String listName, String idMovie);

    List<MovieList> getMovieListsNameByUser(String currentUser);

    List<String> getListsThatContainsCurrentMovie(String idMovie, String currentUser);

    void removeMovieFromList(String movieTitle, String listName, String currentUser);

    boolean checkIfListAlreadyExists(String newNameList, String username);
}
