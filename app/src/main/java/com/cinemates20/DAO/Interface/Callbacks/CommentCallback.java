package com.cinemates20.DAO.Interface.Callbacks;

import com.cinemates20.Model.Comment;

import java.util.List;

public interface CommentCallback {
    default void setAuthorList(List<com.cinemates20.Model.Comment> listAuthor) {}
    default void setComment(Comment comment) {}
}