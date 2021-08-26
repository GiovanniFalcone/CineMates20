package com.cinemates20.Presenter;

import android.os.Bundle;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.R;
import com.cinemates20.View.MovieListUserFragment;
import com.cinemates20.View.MyListsFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class MyListsPresenter {

    private MyListsFragment myListsFragment;

    public MyListsPresenter(MyListsFragment myListsFragment){
        this.myListsFragment = myListsFragment;
    }

    public void myListClicked() {
        UserDAO_Firestore userDao = new UserDAO_Firestore(myListsFragment.getContext());

        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        List<String> movieLists = userDao.getMovieListsNameByUser(currentUser);

        myListsFragment.setRecycler(movieLists);
    }

    public void onClickSeeList(String listClicked) {
        MovieListUserFragment movieListUserFragment = new MovieListUserFragment();
        Bundle args = new Bundle();
        args.putString("listClicked", listClicked);
        movieListUserFragment.setArguments(args);
        myListsFragment.requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, movieListUserFragment)
                .addToBackStack(null)
                .commit();
    }


    public void onClickNewList(String newNameList) {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        UserDAO_Firestore userDAO = new UserDAO_Firestore(myListsFragment.getContext());
        userDAO.addCustomList(newNameList, currentUser);
    }
}
