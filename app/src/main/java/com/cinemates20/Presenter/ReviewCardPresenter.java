package com.cinemates20.Presenter;

import android.os.Bundle;

import com.cinemates20.DAO.Implements.CommentDAO_Firestore;
import com.cinemates20.DAO.Implements.ReviewDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.Model.Comment;
import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;
import com.cinemates20.View.ReactionsTabFragment;
import com.cinemates20.View.UsersReactionsFragment;
import com.cinemates20.View.ReviewCardActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

public class ReviewCardPresenter {

    private final ReviewCardActivity reviewCardActivity;
    private String id;
    private CommentDAO_Firestore commentDAO;

    public ReviewCardPresenter(ReviewCardActivity reviewCardActivity){
        this.reviewCardActivity = reviewCardActivity;
    }

    public void viewReview(){
        ReviewDAO reviewDAO = new ReviewDAO_Firestore(reviewCardActivity.getApplicationContext());
        String author = reviewCardActivity.getAuthor();
        String titleMovie = reviewCardActivity.getTitleMovie();
        Review review = reviewDAO.getReviewByAuthor(author, titleMovie);
        reviewCardActivity.setReview(review.getTextReview());
        id = review.getIdReview();
        UserDAO_Firestore userDao = new UserDAO_Firestore(reviewCardActivity.getApplicationContext());
        User currentUser = userDao.getUsername(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        String reactionType = reviewDAO.getReaction(currentUser.getUsername(), id);
        if(reactionType != null) {
            reviewCardActivity.setColorButton(reactionType);
            reviewCardActivity.setFlag(reactionType);
        }
    }

    public String getId(){
        return id;
    }

    /**
     * If buttonState of buttonType clicked is true add reaction, otherise remove it
     * @param buttonState
     * @param buttonType
     */
    public void manageReactionClicked(boolean buttonState, String buttonType){
        ReviewDAO reviewDAO = new ReviewDAO_Firestore(reviewCardActivity.getApplicationContext());
        UserDAO_Firestore userDao = new UserDAO_Firestore(reviewCardActivity.getApplicationContext());

        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        String idReview = getId();

        User user = userDao.getUsername(currentUser);

        if (!buttonState){
            reviewDAO.addReaction(idReview, buttonType, user.getUsername());
        } else{
            reviewDAO.removeReaction(idReview, buttonType, user.getUsername());
        }
    }

    public void setUserCommentByReview(){
        commentDAO = new CommentDAO_Firestore(reviewCardActivity.getApplication());
        List<Comment> commentList = commentDAO.getUserCommentByReview(reviewCardActivity.getIdReview());
        reviewCardActivity.setRecycler(commentList);
    }

    /**
     * Open bottom sheet dialog and pass idReview to it
     */
    public void onClickNumberReactions(){
        ReactionsTabFragment reactionsTabFragment = new ReactionsTabFragment();
        //Get id review
        String id = getId();
        //Pass the id to fragment dialog and show it
        UsersReactionsFragment usersReactionsFragment = new UsersReactionsFragment();
        Bundle arg = new Bundle();
        arg.putString("idReview", id);
        reactionsTabFragment.setArguments(arg);
        reactionsTabFragment.show(reviewCardActivity.getSupportFragmentManager(), "TAG");
    }
}
