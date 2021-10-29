package com.cinemates20.Model.DAO;

import com.cinemates20.Model.DAO.Implements.CommentDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.FeedDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.MovieListDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.NotificationDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.ReportDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.ReviewDAO_Firestore;
import com.cinemates20.Model.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.Model.DAO.Interface.Firestore.CommentDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.FeedDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.MovieListDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.ReportDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.UserDAO;

public class FirebaseDAOFactory extends DAOFactory {
    @Override
    public UserDAO getUserDAO() {
        return new UserDAO_Firestore();
    }

    @Override
    public ReviewDAO getReviewDAO() {
        return new ReviewDAO_Firestore();
    }

    @Override
    public NotificationDAO getNotificationDAO() {
        return new NotificationDAO_Firestore();
    }

    @Override
    public CommentDAO getCommentDAO() {
        return new CommentDAO_Firestore();
    }

    @Override
    public MovieListDAO getMovieListDAO() {
        return new MovieListDAO_Firestore();
    }

    @Override
    public ReportDAO getReportDAO() {
        return new ReportDAO_Firestore();
    }

    @Override
    public FeedDAO getFeedDAO() {
        return new FeedDAO_Firestore();
    }
}
