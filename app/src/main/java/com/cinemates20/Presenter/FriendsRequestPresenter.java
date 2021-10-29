package com.cinemates20.Presenter;

import android.content.Context;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Firestore.FeedDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.View.FriendListFragment;
import com.cinemates20.View.NotificationFragment;
import com.cinemates20.View.SearchUserTabFragment;
import com.cinemates20.View.UsersReactionsFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.Objects;

public class FriendsRequestPresenter {

    private SearchUserTabFragment searchUserTabFragment;
    private NotificationFragment notificationFragment;
    private FriendListFragment friendListFragment;
    private UsersReactionsFragment usersReactionsFragment;
    private final Context context;

    public FriendsRequestPresenter(SearchUserTabFragment searchUserTabFragment, Context context) {
        this.searchUserTabFragment = searchUserTabFragment;
        this.context = context;
    }

    public FriendsRequestPresenter(NotificationFragment notificationFragment, Context context) {
        this.notificationFragment = notificationFragment;
        this.context = context;
    }

    public FriendsRequestPresenter(FriendListFragment friendListFragment, Context context) {
        this.friendListFragment = friendListFragment;
        this.context = context;
    }

    public FriendsRequestPresenter(UsersReactionsFragment usersReactionsFragment, Context context) {
        this.usersReactionsFragment = usersReactionsFragment;
        this.context = context;
    }

    /**
     * Send friend request to the user if the button type equals "notFriend", delete it otherwise
     * @param userWhoReceivedRequest user who will receive the request/user who already received the request
     * @param buttonState the flag associated to the button: "notFriend"/"appendRequest"
     */
    public void manageSentOrDeleteFriendRequest(String userWhoReceivedRequest, String buttonState) {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        UserDAO userDAO = daoFactory.getUserDAO();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();

        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        if (buttonState.equals("notFriend")) {
            notificationDAO.addRequest(currentUser, userWhoReceivedRequest);
            userDAO.addRequestSent(currentUser, userWhoReceivedRequest);
        }else{
            notificationDAO.removeRequestReceived(currentUser, userWhoReceivedRequest);
            userDAO.removeRequestSent(currentUser, userWhoReceivedRequest);
        }
    }

    /**
     * Remove the friend from the friends list of both the current user and the deleted friend,
     * then remove the previously sent notification
     * @param friendToRemove  the user to remove from friend list
     */
    public void manageRemoveFriendship(String friendToRemove){
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        UserDAO userDAO = daoFactory.getUserDAO();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();

        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

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
     */
    public void manageAcceptOrDeclineFriendRequest(String userWhoSentRequest, String buttonType){
        // create the required DAO Factory
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        UserDAO userDAO = daoFactory.getUserDAO();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();

        String userWhoReceivedRequest = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        if (buttonType.equals("confirm")){
            Timestamp dateAndTime = new Timestamp(new Date());

            notificationDAO.sendNotificationAccepted(userWhoSentRequest, userWhoReceivedRequest, dateAndTime);
            userDAO.addFriend(userWhoReceivedRequest, userWhoSentRequest);
            userDAO.addFriend(userWhoSentRequest, userWhoReceivedRequest);

            FeedDAO feedDAO = daoFactory.getFeedDAO();
            feedDAO.addNews(userWhoReceivedRequest, userWhoSentRequest, "", "", "friendship", 0f, dateAndTime);
        }

        notificationDAO.removeRequestReceived(userWhoSentRequest, userWhoReceivedRequest);
        userDAO.removeRequestSent(userWhoSentRequest, userWhoReceivedRequest);
    }

}
