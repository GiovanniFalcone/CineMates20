package com.cinemates20.DAO.Interface.Firestore;

import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;

import java.util.List;

public interface ReviewDAO {

    void saveReview(String username, String text, int idMovie, String titleMovie);

    void getUserReviewByMovie(String titleMovie);

    Review getReviewByAuthor(String author, String titleMovie);

    void addReaction(String idReview, String buttonType, String username);

    void removeReaction(String idReview, String buttonType, String username);

    String getReaction(String currentUser, String idReview);

    List<User> getListNumberReactions(String buttonType, String idReview);

    List<Review> getListReviews(String currentUser);
}
