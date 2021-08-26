package com.cinemates20.DAO.Interface.Callbacks;

import com.cinemates20.Model.Review;

import java.util.List;

public interface ReviewCallback {
    default void setAuthorList(List<Review> listAuthor) {}
    default void setReview(Review review) {}
}
