package com.cinemates20.DAO.Interface.Firestore;

import android.content.Context;

import com.cinemates20.Model.Comment;

import java.util.List;

public interface CommentDAO {
    void saveComment(String idReview, String author, String comment);
    List<Comment> getUserCommentByReview(String titleMovie, Context context);
    void getCommentByAuthor(String author, String titleMoview);
}