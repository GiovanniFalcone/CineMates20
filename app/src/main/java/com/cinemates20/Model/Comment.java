package com.cinemates20.Model;

import java.util.Date;

public class Comment {

    private String textComment, author, idReview, idComment;
    private Date dateAndTime;
    private boolean visible;

    public Comment(String textComment, String author, String idReview, String idComment, Date dateAndTime, boolean visible) {
        this.textComment = textComment;
        this.author = author;
        this.idReview = idReview;
        this.idComment = idComment;
        this.dateAndTime = dateAndTime;
        this.visible = visible;
    }

    public Comment() {
    }

    public String getIdReview() {
        return idReview;
    }

    public void setIdReview(String idReview) {
        this.idReview = idReview;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public Date getDateAndTime() {
        return (Date) dateAndTime.clone();
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getTextComment() {
        return textComment;
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "textComment='" + textComment + '\'' +
                ", author='" + author + '\'' +
                ", idReview='" + idReview + '\'' +
                ", idComment='" + idComment + '\'' +
                ", dateAndTime=" + dateAndTime +
                ", visible=" + visible +
                '}';
    }
}
