package com.cinemates20.Presenter;

import android.os.Bundle;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.MovieListDAO;
import com.cinemates20.Model.MovieList;
import com.cinemates20.Model.User;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.MovieListUserFragment;
import com.cinemates20.View.MyListsFragment;

import java.util.ArrayList;
import java.util.List;

public class MyListsPresenter {

    private final MyListsFragment myListsFragment;
    private List<MovieList> movieLists;

    public MyListsPresenter(MyListsFragment myListsFragment){
        this.myListsFragment = myListsFragment;
    }

    public void myListClicked() {
        String username = User.getCurrentUser();

        MovieListDAO movieListDAO = DAOFactory.getMovieListDAO(DAOFactory.FIREBASE);
        movieLists = movieListDAO.getMovieListsNameByUser(username);

        myListsFragment.setRecycler(movieLists);
    }

    public void onClickSeeList(MovieList listClicked) {
        MovieListUserFragment movieListUserFragment = new MovieListUserFragment();
        Bundle args = new Bundle();
        args.putString("nameListClicked", listClicked.getNameList());
        args.putString("descriptionListClicked", listClicked.getDescription());
        args.putString("idListClicked", listClicked.getIdMovieList());
        args.putIntegerArrayList("idMovieList", (ArrayList<Integer>) listClicked.getListIDMovie());
        movieListUserFragment.setArguments(args);
        myListsFragment.requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, movieListUserFragment)
                .addToBackStack(null)
                .commit();
    }


    public void onClickNewList(String newNameList, String description) {
        String username = User.getCurrentUser();

        MovieListDAO movieListDAO = DAOFactory.getMovieListDAO(DAOFactory.FIREBASE);
        boolean isExists = movieListDAO.checkIfListAlreadyExists(newNameList, username);
        if(!isExists) {
            movieListDAO.addCustomList(newNameList, description, username);

            MovieList movieList = new MovieList(null, newNameList, description, username, null);
            movieLists.add(movieList);
            myListsFragment.updateRecycler();
        } else
            Utils.showErrorDialog(myListsFragment.getContext(), "Cannot create the list", "You already have a list with the same name!");
    }
}
