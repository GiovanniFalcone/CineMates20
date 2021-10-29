package com.cinemates20.Model.DAO.Interface.Callbacks;

import java.util.List;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

public interface MovieCallback {

    default void setResult(MovieDb movieDb) {}

    default void setMovie(List<MovieDb> movieDbList) {}

    default void setGenre(List<Genre> genreList) {}

    default void setLogo(String pathLogo) {}

    default void setArtworks(List<Artwork> artworks) {}

    default void setCast(List<PersonCast> personCast) {}
}
