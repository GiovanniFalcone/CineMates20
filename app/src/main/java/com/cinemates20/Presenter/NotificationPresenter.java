package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.Model.Notification;
import com.cinemates20.View.NotificationFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class NotificationPresenter {

    private final NotificationFragment notificationFragment;

    public NotificationPresenter(NotificationFragment notificationFragment){
        this.notificationFragment = notificationFragment;
    }

    public void notificationClicked(){
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();
        notificationDAO.changeNotificationState(currentUser);

        List<Notification> notificationList = notificationDAO.getNotifications(currentUser);
        notificationFragment.setRecycler(notificationList);
    }
}
