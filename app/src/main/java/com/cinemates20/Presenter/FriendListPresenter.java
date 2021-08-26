package com.cinemates20.Presenter;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.View.FriendListFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class FriendListPresenter {

    private FriendListFragment friendListFragment;
    private UserDAO_Firestore userDAO;

    public FriendListPresenter(FriendListFragment friendListFragment){
        this.friendListFragment = friendListFragment;
    }

    /**
     * Get friend list of current user and show it into recycler view
     */
    public void friendListClicked(){
        userDAO = new UserDAO_Firestore(friendListFragment.getContext());
        List<String> friendsList = userDAO.getFriends(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        friendListFragment.setRecycler(friendsList);
    }

}
