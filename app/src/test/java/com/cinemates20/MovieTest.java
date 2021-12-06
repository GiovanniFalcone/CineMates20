package com.cinemates20;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Implements.MovieDAO_TMDB;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieTest {

    private static final int ID_MOVIE_BLADE_RUNNER = 78;
    private static final int ID_MOVIE_THE_AVENGERS = 24428;
    private List<Integer> list;
/*
    @BeforeEach
    public void setUp(){
        list = new ArrayList<>();
    }

    @Test
    public void getMoviesOfList_expectedTrue(){
        list.add(ID_MOVIE_BLADE_RUNNER);
        list.add(ID_MOVIE_THE_AVENGERS);

        MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
        List<MovieDb> movieDbList = movieDAO.getMoviesOfList(list);

        Assertions.assertTrue(movieDbList.size() > 0);
    }

    @Test
    public void getMoviesOfList_expectedFalse(){
        MovieDAO movieDAO = DAOFactory.getMovieDAO(DAOFactory.TMDB);
        List<MovieDb> movieDbList = movieDAO.getMoviesOfList(list);

        Assertions.assertFalse( movieDbList.size() > 0);
    }*/
}
