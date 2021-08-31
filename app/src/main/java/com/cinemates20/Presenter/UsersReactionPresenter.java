package com.cinemates20.Presenter;

import com.cinemates20.DAO.Implements.ReviewDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.UsersReactionsFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsersReactionPresenter {

    private UsersReactionsFragment usersReactionsFragment;

    public UsersReactionPresenter (UsersReactionsFragment usersReactionsFragment){
        this.usersReactionsFragment = usersReactionsFragment;
    }

    /**
     * Displays the list of users who have clicked the reaction in question,
     * depending on the selected tab
     */
    public void onClickReactionType(){
        List<User> userList = new ArrayList<>();
        List<String> friendListReaction = new ArrayList<>();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        UserDAO_Firestore userDAO = new UserDAO_Firestore(usersReactionsFragment.getContext());
        List<String> friendList = userDAO.getFriends(currentUser);
        ReviewDAO reviewDAO = new ReviewDAO_Firestore(usersReactionsFragment.getContext());
        String id = usersReactionsFragment.getIdReview();
        int index = usersReactionsFragment.getIndexTab();

        switch (index) {
            case 0:
                userList = reviewDAO.getListNumberReactions("like", id);
                break;
            case 1:
                userList = reviewDAO.getListNumberReactions("dislike", id);
                break;
            case 2:
                userList = reviewDAO.getListNumberReactions("love", id);
                break;
            case 3:
                userList = reviewDAO.getListNumberReactions("clap", id);
                break;
            case 4:
                userList = reviewDAO.getListNumberReactions("grrr", id);
                break;
        }
        usersReactionsFragment.setRecycler(userList);
    }

}
