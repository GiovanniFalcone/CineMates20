package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.CommentDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.FeedDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.ReviewCardActivity;
import com.google.firebase.Timestamp;

import java.util.Date;

public class WriteCommentPresenter {

    private final ReviewCardActivity reviewCardActivity;

    public WriteCommentPresenter(ReviewCardActivity reviewCardActivity){
        this.reviewCardActivity = reviewCardActivity;
    }

    public void clickAddComment(String textComment, String idReview) {
        String authorComment = User.getCurrentUser();
        Timestamp dateAndTime = new Timestamp(new Date());
        CommentDAO commentDAO = DAOFactory.getCommentDAO(DAOFactory.FIREBASE);
        commentDAO.saveComment(idReview, authorComment, textComment, dateAndTime);

        //save comment into feed
        FeedDAO feedDAO = DAOFactory.getFeedDAO(DAOFactory.FIREBASE);
        feedDAO.addNews(authorComment, reviewCardActivity.getReview().getAuthor(), "", idReview, "comment", 0f, dateAndTime);
    }
}
