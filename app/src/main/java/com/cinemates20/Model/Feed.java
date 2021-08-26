package com.cinemates20.Model;

import java.util.List;

public class Feed {

    private List<Review> reviewList;
    private List<Comment> commentList;
    private List<Notification> requestAcceptedList;

    public Feed() {}

    public Feed(List<Review> reviewList, List<Comment> commentList, List<Notification> requestAcceptedList) {
        this.reviewList = reviewList;
        this.commentList = commentList;
        this.requestAcceptedList = requestAcceptedList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Notification> getRequestAcceptedList() {
        return requestAcceptedList;
    }

    public void setRequestAcceptedList(List<Notification> requestAcceptedList) {
        this.requestAcceptedList = requestAcceptedList;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "reviewList=" + reviewList +
                "\n"+
                ", commentList=" + commentList +
                "\n" +
                ", requestAcceptedList=" + requestAcceptedList +
                '}';
    }
}
