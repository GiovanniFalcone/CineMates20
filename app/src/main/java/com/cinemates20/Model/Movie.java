package com.cinemates20.Model;

import androidx.annotation.NonNull;

import java.util.List;

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.people.PersonCast;

public class Movie {

    private int id;
    private String title, posterPath, backdropPath, overview;
    private List<Genre> genreList;
    private List<PersonCast> personCasts;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
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
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", overview='" + overview + '\'' +
                ", genreList=" + genreList +
                ", personCasts=" + personCasts +
                '}';
    }
}
