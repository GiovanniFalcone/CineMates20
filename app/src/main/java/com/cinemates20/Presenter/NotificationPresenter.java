package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.NotificationDAO;
import com.cinemates20.Model.Notification;
import com.cinemates20.Model.User;
import com.cinemates20.View.NotificationFragment;

import java.util.List;

public class NotificationPresenter {

    private final NotificationFragment notificationFragment;

    public NotificationPresenter(NotificationFragment notificationFragment){
        this.notificationFragment = notificationFragment;
    }

    public void notificationClicked(){
        String currentUser = User.getCurrentUser();

        NotificationDAO notificationDAO = DAOFactory.getNotificationDAO(DAOFactory.FIREBASE);
        notificationDAO.changeNotificationState(currentUser);

        List<Notification> notificationList = notificationDAO.getNotifications(currentUser);
        notificationFragment.setRecycler(notificationList);
    }
}
