package com.cinemates20.Model;

import androidx.annotation.NonNull;

import java.util.List;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

public class Movie {

    private MovieDb movieDb;
    private List<Genre> genreList;
    private List<PersonCast> personCasts;

    public MovieDb getMovieDb() {
        return movieDb;
    }

    public void setMovieDb(MovieDb movieDb) {
        this.movieDb = movieDb;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public List<PersonCast> getPersonCasts() {
        return personCasts;
    }

    public void setPersonCasts(List<PersonCast> personCasts) {
        this.personCasts = personCasts;
    }

    @NonNull
    @Override
    public String toString() {
        return "Movie{" +
                "movieDb=" + movieDb +
                ", genreList=" + genreList +
                ", personCasts=" + personCasts +
                '}';
    }
}
