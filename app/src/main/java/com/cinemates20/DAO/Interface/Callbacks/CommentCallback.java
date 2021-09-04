package com.cinemates20.DAO.Interface.Callbacks;

import com.cinemates20.Model.Comment;

import java.util.List;

public interface CommentCallback {
    default void setNewComments(Comment comment) {}
    default void setComment(Comment comment) {}
}