package com.cinemates20.DAO.Interface.Firestore;

import com.cinemates20.DAO.Interface.Callbacks.NotificationCallback;
import com.cinemates20.Model.Notification;

import java.util.List;

public interface NotificationDAO {
    void changeNotificationState(String currentUser);

    void addRequest(String currentUser, String userWhoReceivedRequest);

    void removeRequestReceived(String currentUser, String userWhoReceivedRequest);

    void removeNotificationOfAcceptedRequest(String currentUser, String userWhoReceivedNotification);

    List<String> getRequestReceived(String userWhoRecivedRequest, String currentUser);

    void sendNotificationAccepted(String currentUser, String userAdded);

    List<Notification> getNotifications(String currentUser);

    List<Notification> updateNotifications(String currentUser, NotificationCallback notificationCallback);
}
