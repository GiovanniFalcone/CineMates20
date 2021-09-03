package com.cinemates20.Presenter;

import com.cinemates20.DAO.Implements.CommentDAO_Firestore;
import com.cinemates20.DAO.Implements.ReviewDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.CommentDAO;
import com.cinemates20.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.Comment;
import com.cinemates20.View.ReviewCardActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.Objects;

public class WriteCommentPresenter {

    private final ReviewCardActivity reviewCardActivity;
    private CommentDAO commentDAO;
    private UserDAO userDAO;
    private ReviewDAO reviewDAO;

    public WriteCommentPresenter(ReviewCardActivity reviewCardActivity){
        this.reviewCardActivity = reviewCardActivity;
    }

    public void clickAddComment(String textComment) {
        reviewDAO = new ReviewDAO_Firestore(reviewCardActivity.getApplicationContext());
        userDAO = new UserDAO_Firestore(reviewCardActivity.getApplicationContext());
        commentDAO = new CommentDAO_Firestore(reviewCardActivity.getApplicationContext());

        String idReview = reviewCardActivity.getIdReview();
        String authorComment = userDAO.getUsername(Objects
                .requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).getUsername();

        commentDAO.saveComment(idReview, authorComment, textComment);

        Comment comment = new Comment(textComment, authorComment, idReview, null, new Date(), true);
        reviewCardActivity.updateRecycler(comment);


    }
}
