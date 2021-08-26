package com.cinemates20.Model;

import java.util.Date;

public class Comment {

    private String textComment, author, idReview, idComment;
    private Date timeAndDate;
    private boolean visible;

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

    public Date getTimeAndDate() {
        return timeAndDate;
    }

    public void setTimeAndDate(Date timeAndDate) {
        this.timeAndDate = timeAndDate;
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
                ", timeAndDate=" + timeAndDate +
                ", visible=" + visible +
                '}';
    }
}
