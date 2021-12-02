package com.cinemates20.Model.DAO;

import com.cinemates20.Model.DAO.Implements.CommentDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.FeedDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.MovieDAO_TMDB;
import com.cinemates20.Model.DAO.Implements.MovieListDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.NotificationDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.ReportDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.ReviewDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.CommentDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.FeedDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.MovieListDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.NotificationDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReportDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.DAO.Interface.TMDB.MovieDAO;

import java.util.Locale;

public class DAOFactory {

    public static final String FIREBASE = "firebase";

    public static final String TMDB = "tmdb";

    public static UserDAO getUserDAO(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case "firebase":
                return new UserDAO_Firestore();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static ReviewDAO getReviewDAO(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case FIREBASE:
                return new ReviewDAO_Firestore();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static NotificationDAO getNotificationDAO(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case FIREBASE:
                return new NotificationDAO_Firestore();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static CommentDAO getCommentDAO(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case FIREBASE:
                return new CommentDAO_Firestore();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static FeedDAO getFeedDAO(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case FIREBASE:
                return new FeedDAO_Firestore();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static ReportDAO getReportDAO(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case FIREBASE:
                return new ReportDAO_Firestore();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static MovieListDAO getMovieListDAO(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case FIREBASE:
                return new MovieListDAO_Firestore();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static MovieDAO getMovieDAO(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case TMDB:
                return new MovieDAO_TMDB();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
