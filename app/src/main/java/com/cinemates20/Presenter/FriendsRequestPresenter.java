package com.cinemates20.Presenter;

import android.content.Context;

import com.cinemates20.DAO.Implements.NotificationDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.FriendListFragment;
import com.cinemates20.View.NotificationFragment;
import com.cinemates20.View.SearchUserTabFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class FriendsRequestPresenter {

    private SearchUserTabFragment searchUserTabFragment;
    private NotificationFragment notificationFragment;
    private FriendListFragment friendListFragment;
    private NotificationDAO notificationDAO;
    private UserDAO userDAO;

    public FriendsRequestPresenter(SearchUserTabFragment searchUserTabFragment) {
        this.searchUserTabFragment = searchUserTabFragment;
    }

    public FriendsRequestPresenter(NotificationFragment notificationFragment) {
        this.notificationFragment = notificationFragment;
    }

    public FriendsRequestPresenter(FriendListFragment friendListFragment) {
        this.friendListFragment = friendListFragment;
    }

    /**
     * Send friend request to the user if the button type equals "notFriend", delete it otherwise
     * @param userWhoReceivedRequest - user who will receive the request/user who already received
     *                               the request
     * @param buttonState - the flag associated to the button: "notFriend"/"appendRequest"
     */
    public void manageSentOrDeleteFriendRequest(String userWhoReceivedRequest, String buttonState) {
        userDAO = new UserDAO_Firestore(searchUserTabFragment.requireContext());
        notificationDAO = new NotificationDAO_Firestore(searchUserTabFragment.requireContext());

        User currentUser = userDAO.getUsername(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        if (buttonState.equals("notFriend")) {
            notificationDAO.addRequest(currentUser.getUsername(), userWhoReceivedRequest);
            userDAO.addRequestSent(currentUser.getUsername(), userWhoReceivedRequest);
        }else{
            notificationDAO.removeRequestReceived(currentUser.getUsername(), userWhoReceivedRequest);
            userDAO.removeRequestSent(currentUser.getUsername(), userWhoReceivedRequest);
        }
    }

    /**
     * Remove the friend from the friends list of both the current user and the deleted friend,
     * then remove the previously sent notification
     * @param friendToRemove - the user to remove from friend list
     * @param context - the context, the function can be called in multiple fragment
     */
    public void manageRemoveFriendship(String friendToRemove, Context context){
        userDAO = new UserDAO_Firestore(context);
        notificationDAO = new NotificationDAO_Firestore(context);

        String currentUser = userDAO.getUsername(Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getEmail()).getUsername();

        userDAO.removeFriend(currentUser, friendToRemove);
        userDAO.removeFriend(friendToRemove, currentUser);

        notificationDAO.removeNotificationOfAcceptedRequest(currentUser, friendToRemove);
        notificationDAO.removeNotificationOfAcceptedRequest(friendToRemove, currentUser);
    }

    /**
     * The function manage the friend request.
     * If the user accept the request send notification to user who sent the request and add
     * the person to friend list of both users.
     * If user accept or decline the request the function will remove the request sent from the user
     * who sent the request and the request received from user who received it.
     * @param userWhoSentRequest - user who sent the request
     * @param buttonType - the type of button clicked: confirm/remove
     * @param context - the context, the function can be called in multiple fragment
     */
    public void manageAcceptOrDeclineFriendRequest(String userWhoSentRequest, String buttonType, Context context){
        userDAO = new UserDAO_Firestore(context);
        notificationDAO = new NotificationDAO_Firestore(context);

        String userWhoReceivedRequest = userDAO.getUsername(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).getUsername();

        if (buttonType.equals("confirm")){
            notificationDAO.sendNotificationAccepted(userWhoSentRequest, userWhoReceivedRequest);
            userDAO.addFriend(userWhoReceivedRequest, userWhoSentRequest);
            userDAO.addFriend(userWhoSentRequest, userWhoReceivedRequest);
        }

        notificationDAO.removeRequestReceived(userWhoSentRequest, userWhoReceivedRequest);
        userDAO.removeRequestSent(userWhoSentRequest, userWhoReceivedRequest);
    }

}
