package com.cinemates20.Model.DAO.Interface.TMDB;

import com.cinemates20.Model.Movie;

import java.util.List;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.people.PersonCast;

public interface MovieDAO {

    Movie getMovieById(int idMovie);

    List<Movie> getMovies(String query);

    List<Movie> getPopular();

    List<Movie> getNowPlaying();

    List<Movie> getTopRated();

    List<Genre> getGenre(int idMovie);

    List<Movie> getListMovieByGenre(int idGenre, int page);

    List<Artwork> getBackdrops(int idMovie);

    List<PersonCast> getCast(int idMovie);

    List<Movie> getMoviesOfList(List<Integer> idMoviesList);

    String getLogo(int idMovie);
}
