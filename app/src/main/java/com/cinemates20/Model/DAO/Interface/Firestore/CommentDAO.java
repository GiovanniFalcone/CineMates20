package com.cinemates20.Model.DAO.Interface.Firestore;

import android.content.Context;

import com.cinemates20.Model.DAO.Interface.Callbacks.CommentCallback;
import com.google.firebase.Timestamp;

public interface CommentDAO {
    void saveComment(String idReview, String author, String comment, Timestamp dateAndTime);

    void getUserCommentByReview(String titleMovie, Context context, CommentCallback commentCallback);

    void updateCounter(String idComment, String reportType);

    void changeState(String idComment, String reportType);

    Number getCounter(String idComment, String reportType);
}