package com.cinemates20.Model.DAO.Interface.TMDB;

import com.cinemates20.Model.DAO.Interface.Callbacks.MovieCallback;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public interface MovieDAO {

    void getMovieById(int idMovie, MovieCallback movieCallback);

    void getMovies(String query, MovieCallback movieCallback);

    void getPopular(MovieCallback movieCallback);

    void getNowPlaying(MovieCallback movieCallback);

    void getTopRated(MovieCallback movieCallback);

    void getGenre(int idMovie, MovieCallback movieCallback);

    void getListMovieByGenre(int idGenre, int page, MovieCallback movieCallback);

    void getBackdrops(int idMovie, MovieCallback movieCallback);

    void getCast(int idMovie, MovieCallback movieCallback);

    List<MovieDb> getMoviesOfList(List<Integer> idMoviesList);

    void getLogo(int idMovie, MovieCallback movieCallback);
}
