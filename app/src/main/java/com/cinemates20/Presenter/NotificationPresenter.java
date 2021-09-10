package com.cinemates20.Presenter;

import android.util.Log;

import com.cinemates20.DAO.Implements.NotificationDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.Model.Notification;
import com.cinemates20.View.NotificationFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class NotificationPresenter {

    private NotificationDAO notificationDAO;
    private NotificationFragment notificationFragment;
    private UserDAO_Firestore userDAO;

    public NotificationPresenter(NotificationFragment notificationFragment){
        this.notificationFragment = notificationFragment;
    }

    public void notificationClicked(){
        userDAO = new UserDAO_Firestore(notificationFragment.getContext());
        String currentUser = userDAO.getUsername(FirebaseAuth.getInstance().getCurrentUser().getEmail()).getUsername();
        notificationDAO = new NotificationDAO_Firestore(notificationFragment.getContext());
        notificationDAO.changeNotificationState(currentUser);

        List<Notification> notificationList = notificationDAO.getNotifications(currentUser);
        notificationFragment.setRecycler(notificationList);
    }
}
