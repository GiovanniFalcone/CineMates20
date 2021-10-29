package com.cinemates20.Presenter;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Firestore.CommentDAO;
import com.cinemates20.Model.DAO.Interface.Firestore.FeedDAO;
import com.cinemates20.View.ReviewCardActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.Objects;

public class WriteCommentPresenter {

    private final ReviewCardActivity reviewCardActivity;

    public WriteCommentPresenter(ReviewCardActivity reviewCardActivity){
        this.reviewCardActivity = reviewCardActivity;
    }

    public void clickAddComment(String textComment) {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);

        String idReview = reviewCardActivity.getReview().getIdReview();
        String authorComment = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        Timestamp dateAndTime = new Timestamp(new Date());
        CommentDAO commentDAO = daoFactory.getCommentDAO();
        commentDAO.saveComment(idReview, authorComment, textComment, dateAndTime);

        //save comment into feed
        FeedDAO feedDAO = daoFactory.getFeedDAO();
        feedDAO.addNews(authorComment, reviewCardActivity.getReview().getAuthor(), "", idReview, "comment", 0f, dateAndTime);
    }
}
