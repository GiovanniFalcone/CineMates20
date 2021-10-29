package com.cinemates20.Model.DAO.Interface.Callbacks;

public interface ReviewCallback {

    default void setRating(float rating, int total) {}
    default void onSuccess(String idReview) {}
}
