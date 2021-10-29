package com.cinemates20.Model;

import java.util.List;

public class MovieList {

    private String idMovieList, nameList, description, username;
    private List<Integer> idMovie;

    public MovieList(){}

    public MovieList(String idMovieList, String nameList, String description, String username, List<Integer> idMovie) {
        this.idMovieList = idMovieList;
        this.nameList = nameList;
        this.description = description;
        this.username = username;
        this.idMovie = idMovie;
    }

    public String getIdMovieList() {
        return idMovieList;
    }

    public void setIdMovieList(String idMovieList) {
        this.idMovieList = idMovieList;
    }

    public String getNameList() {
        return nameList;
    }

    public void setNameList(String nameList) {
        this.nameList = nameList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getIdMovies() {
        return idMovie;
    }

    public void setIdMovies(List<Integer> idMovies) {
        this.idMovie = idMovies;
    }

    @Override
    public String toString() {
        return "MovieList{" +
                "idMovieList='" + idMovieList + '\'' +
                ", nameList='" + nameList + '\'' +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                ", idMovies=" + idMovie +
                '}';
    }
}
