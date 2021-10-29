package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.View.FriendListFragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class FriendListPresenter {

    private final FriendListFragment friendListFragment;

    public FriendListPresenter(FriendListFragment friendListFragment){
        this.friendListFragment = friendListFragment;
    }

    /**
     * Get friend list of current user and show it into recycler view
     */
    public void friendListClicked(){
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        UserDAO userDAO = daoFactory.getUserDAO();

        List<String> friendsList = userDAO.getFriends(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        if(!friendsList.isEmpty()) {
            friendListFragment.setRecycler(friendsList);
            friendListFragment.setNoFriend(false);
        }else
            friendListFragment.setNoFriend(true);
    }
}
