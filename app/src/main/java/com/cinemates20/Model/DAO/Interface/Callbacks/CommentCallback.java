package com.cinemates20.Model.DAO.Interface.Callbacks;

import com.cinemates20.Model.Comment;

public interface CommentCallback {

    default void setNewComments(Comment comment) {}

}