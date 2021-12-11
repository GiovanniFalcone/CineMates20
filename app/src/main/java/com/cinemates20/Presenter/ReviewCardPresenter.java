package com.cinemates20.Presenter;

import android.os.Bundle;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Callbacks.CommentCallback;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.CommentDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.NotificationDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.ReviewDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.Comment;
import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.ReactionsTabFragment;
import com.cinemates20.View.ReviewCardActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReviewCardPresenter {

    private final ReviewCardActivity reviewCardActivity;

    public ReviewCardPresenter(ReviewCardActivity reviewCardActivity){
        this.reviewCardActivity = reviewCardActivity;
    }

    public void viewReview(Review review){
        reviewCardActivity.setNameAuthorView(review.getAuthor());
        reviewCardActivity.setRatingReview(review.getRating());
        reviewCardActivity.setReview(review.getTextReview());
        //Set icon on toolbar
        reviewCardActivity.setAuthorIcon();
        //Set icon for comment
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);
        User currentUser = userDAO.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        reviewCardActivity.setUserIcon(currentUser.getIcon());

        //Check if the user reacted to the review
        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);
        String reactionType = reviewDAO.getReaction(User.getCurrentUser(), review.getIdReview());
        if(reactionType != null) {
            reviewCardActivity.setColorButton(reactionType);
            reviewCardActivity.setFlag(reactionType);
        }

        setUserCommentByReview(review.getIdReview());
    }

    /**
     * If buttonState of buttonType clicked is true add reaction, otherwise remove it
     * @param buttonState the state of button: already selected or not
     * @param buttonType the reaction that user has clicked
     */
    public void manageReactionClicked(String idReview, boolean buttonState, String buttonType){
        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);

        String user = User.getCurrentUser();

        if (!buttonState){
            reviewDAO.addReaction(idReview, buttonType, user);
        } else{
            reviewDAO.removeReaction(idReview, buttonType, user);
        }
    }

    public void setUserCommentByReview(String idReview){
        List<Comment> commentList = new ArrayList<>();

        CommentDAO commentDAO = DAOFactory.getCommentDAO(DAOFactory.FIREBASE);

        reviewCardActivity.setView(true);

        commentDAO.getUserCommentByReview(idReview, reviewCardActivity.getActivityContext(), new CommentCallback() {
            @Override
            public void setNewComments(Comment comment) {
                if(comment.isVisible()) {
                    commentList.add(comment);
                    reviewCardActivity.setRecycler(commentList);
                    reviewCardActivity.setView(false);
                }
            }
        });
    }

    /**
     * Open bottom sheet dialog and pass idReview to it
     */
    public void onClickNumberReactions(String idReview){
        //Get sent and received request and friend list
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);
        User user = userDAO.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        NotificationDAO notificationDAO = DAOFactory.getNotificationDAO(DAOFactory.FIREBASE);
        List<String> sentList = notificationDAO.getAllRequestSent(user.getUsername());
        List<String> receivedList = notificationDAO.getRequestReceived("", user.getUsername());

        //Pass the id and user info to fragment dialog and show it
        ReactionsTabFragment reactionsTabFragment = new ReactionsTabFragment();
        Bundle arg = new Bundle();
        arg.putSerializable("User", user);
        arg.putStringArrayList("sentList", (ArrayList<String>) sentList);
        arg.putStringArrayList("receivedList", (ArrayList<String>) receivedList);
        arg.putString("idReview", idReview);
        reactionsTabFragment.setArguments(arg);
        reactionsTabFragment.show(reviewCardActivity.getSupportFragmentManager(), "TAG");
    }

    public void rateReview(float valuation, String idReview) {
        ReviewDAO reviewDAO = DAOFactory.getReviewDAO(DAOFactory.FIREBASE);

        String user = User.getCurrentUser();
        boolean isExists = reviewDAO.checkIfValuationExists(idReview, user);

        if (!isExists) {
            reviewDAO.addValuationToUserReview(valuation, idReview, user);
            reviewDAO.updateRatingReview(valuation, idReview, user, "new");
        }else {
            reviewDAO.updateValuationToUserReview(valuation, idReview, user);
            reviewDAO.updateRatingReview(valuation, idReview, user, "modified");
        }

        Utils.showDialog(reviewCardActivity.getActivityContext(), "Done!", "Valuation successfully saved!");
    }
}
