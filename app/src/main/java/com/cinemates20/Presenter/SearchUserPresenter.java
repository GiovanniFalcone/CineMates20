package com.cinemates20.Presenter;

import com.cinemates20.DAO.Implements.NotificationDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.SearchUserTabFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class SearchUserPresenter {

    private SearchUserTabFragment searchUserTabFragment;
    private UserDAO userDAO;
    private NotificationDAO notificationDAO;

    public SearchUserPresenter(SearchUserTabFragment searchUserTabFragment){
        this.searchUserTabFragment = searchUserTabFragment;
    }

    public void onSearchUsers(String query){
        userDAO = new UserDAO_Firestore(searchUserTabFragment.requireContext());
        notificationDAO = new NotificationDAO_Firestore(searchUserTabFragment.requireContext());

        User user = userDAO.getUsername(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        List<User> searchedUserList = userDAO.getListUsername(query, user.getUsername());
        List<String> sentList = userDAO.getRequestSent(query);
        List<String> receivedList = notificationDAO.getRequestReceived(query);
        searchUserTabFragment.setRecycler(searchedUserList, sentList, receivedList, user.getFriends());
    }
}
