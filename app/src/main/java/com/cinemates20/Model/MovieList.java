package com.cinemates20.Model;

public class MovieList {
    private String listName, idMovie;
    //private User user;


    public MovieList(String listName, String idMovie) {
        this.listName = listName;
        this.idMovie = idMovie;
    }

    public MovieList() {
    }


    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    @Override
    public String toString() {
        return "MovieList{" +
                "listName='" + listName + '\'' +
                ", idMovie='" + idMovie + '\'' +
                '}';
    }


}
