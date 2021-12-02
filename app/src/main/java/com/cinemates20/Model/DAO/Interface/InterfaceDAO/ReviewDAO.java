package com.cinemates20.Model.DAO.Interface.InterfaceDAO;

import com.cinemates20.Model.DAO.Interface.Callbacks.ReviewCallback;
import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;
import com.google.firebase.Timestamp;

import java.util.List;

public interface ReviewDAO {

    void saveReview(String username, float valuation, String text, int idMovie, Timestamp dateAndTime, ReviewCallback reviewCallback);

    void addReview(String username, String textReview, int idMovie, Timestamp dateAndTime, ReviewCallback reviewCallback);

    List<Review> getUserReviewByMovie(int idMovie, List<String> friends, String username, boolean seeAllReviewClicked);

    Review getReviewByAuthor(String author, int idMovie);

    Review getReviewById(String idReview);

    void addReaction(String idReview, String buttonType, String username);

    void removeReaction(String idReview, String buttonType, String username);

    String getReaction(String currentUser, String idReview);

    List<User> getListNumberReactions(String buttonType, String idReview);

    List<Review> getListReviews(String currentUser);

    void addValuationToUserReview(float valuation, String idReview, String username);

    boolean checkIfValuationExists(String idReview, String username);

    void updateValuationToUserReview(float valuation, String idReview, String username);

    void updateRatingReview(float valuation, String idReview, String username, String state);

    void getMovieRating(int idMovie, ReviewCallback reviewCallback);

    void updateCounter(String idReview, String reportType);

    void changeState(String idReview, String reportType);
}
