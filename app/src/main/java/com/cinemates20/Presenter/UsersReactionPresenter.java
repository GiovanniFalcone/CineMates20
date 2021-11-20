package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.UsersReactionsFragment;

import java.util.ArrayList;
import java.util.List;

public class UsersReactionPresenter {

    private final UsersReactionsFragment usersReactionsFragment;

    public UsersReactionPresenter (UsersReactionsFragment usersReactionsFragment){
        this.usersReactionsFragment = usersReactionsFragment;
    }

    /**
     * Displays the list of users who have clicked the reaction in question,
     * depending on the selected tab.
     * The user can also send a friend request to them.
     */
    public void onClickReactionType(String idReview){
        List<User> userList = new ArrayList<>();

        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);

        int index = usersReactionsFragment.getIndexTab();

        switch (index) {
            case 0:
                userList = reviewDAO.getListNumberReactions("like", idReview);
                break;
            case 1:
                userList = reviewDAO.getListNumberReactions("dislike", idReview);
                break;
            case 2:
                userList = reviewDAO.getListNumberReactions("love", idReview);
                break;
            case 3:
                userList = reviewDAO.getListNumberReactions("clap", idReview);
                break;
            case 4:
                userList = reviewDAO.getListNumberReactions("grrr", idReview);
                break;
        }

        User currentUsername = usersReactionsFragment.getUser();
        List<String> sentList = usersReactionsFragment.getSentList();
        List<String> receivedList = usersReactionsFragment.getReceivedList();

        usersReactionsFragment.setNumberReactions(userList.size());
        usersReactionsFragment.setRecycler(userList, sentList, receivedList, currentUsername.getFriends(), currentUsername.getUsername());
    }

}
