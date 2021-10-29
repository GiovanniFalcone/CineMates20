package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.SearchUserTabFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

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
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        UserDAO userDAO = daoFactory.getUserDAO();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();

        String username = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        List<User> searchedUserList = userDAO.getListUsername(query, username);
        List<String> sentList = userDAO.getRequestSent(query);
        List<String> receivedList = notificationDAO.getRequestReceived(query, username);
        List<String> friendList = userDAO.getFriends(username);

        searchUserTabFragment.setRecycler(searchedUserList, sentList, receivedList, friendList);
    }
}
