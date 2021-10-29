package com.cinemates20.Model.DAO;

import com.cinemates20.Model.DAO.Interface.Firestore.CommentDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.FeedDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.MovieListDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.ReportDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.UserDAO;

abstract public class DAOFactory {

    // List of DAO types supported by the factory
    public static final int FIREBASE = 1;
    //...

    // There will be a method for each DAO that can be
    // created. The concrete factories will have to
    // implement these methods.
    public abstract UserDAO getUserDAO();
    public abstract ReviewDAO getReviewDAO();
    public abstract NotificationDAO getNotificationDAO();
    public abstract CommentDAO getCommentDAO();
    public abstract MovieListDAO getMovieListDAO();
    public abstract ReportDAO getReportDAO();
    public abstract FeedDAO getFeedDAO();

    public static DAOFactory getDAOFactory(int whichFactory) {

        switch (whichFactory) {
            case FIREBASE:
                return new FirebaseDAOFactory();
            default:
                return null;
        }
    }
}
