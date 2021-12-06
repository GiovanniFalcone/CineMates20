package com.cinemates20.Presenter;

import android.util.Log;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.NotificationDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.SearchUserTabFragment;

import java.util.List;

public class SearchUserPresenter {

    private final SearchUserTabFragment searchUserTabFragment;

    public SearchUserPresenter(SearchUserTabFragment searchUserTabFragment){
        this.searchUserTabFragment = searchUserTabFragment;
    }

    /**
     * This method will get and set the searched users by current user.
     * @param query the user that current user want to search
     */
    public void onSearchUsers(String query){
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);
        NotificationDAO notificationDAO = DAOFactory.getNotificationDAO(DAOFactory.FIREBASE);

        String username = User.getCurrentUser();

        List<User> searchedUserList = userDAO.getListUsername(query, username);
        List<String> friendList = userDAO.getFriends(username);
        List<String> sentList = notificationDAO.getRequestSent(query, username);
        List<String> receivedList = notificationDAO.getRequestReceived(query, username);

        searchUserTabFragment.setRecycler(searchedUserList, sentList, receivedList, friendList);
    }
}
