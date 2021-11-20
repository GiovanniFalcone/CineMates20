package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.FriendListFragment;

import java.util.List;

public class FriendListPresenter {

    private final FriendListFragment friendListFragment;

    public FriendListPresenter(FriendListFragment friendListFragment){
        this.friendListFragment = friendListFragment;
    }

    /**
     * Get friend list of current user and show it into recycler view
     */
    public void friendListClicked(){
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);

        List<String> friendsList = userDAO.getFriends(User.getCurrentUser());
        if(!friendsList.isEmpty()) {
            friendListFragment.setRecycler(friendsList);
            friendListFragment.setNoFriend(false);
        }else
            friendListFragment.setNoFriend(true);
    }
}
