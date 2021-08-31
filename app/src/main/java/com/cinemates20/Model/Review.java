package com.cinemates20.Model;

import java.util.Date;

public class Review {
    private String textReview, author, titleMovie, idReview;
    private Date dateAndTime;
    private boolean visible;
    private int totalLike, totalDislike, totalLove, totalClap, totalGrrr, idMovie;

    public Review() {}

    public Review(String textReview, String author, String titleMovie, String idReview, Date dateAndTime, boolean visible, int totalLike, int totalDislike, int totalLove, int totalClap, int totalGrrr, int idMovie) {
        this.textReview = textReview;
        this.author = author;
        this.titleMovie = titleMovie;
        this.idReview = idReview;
        this.dateAndTime = dateAndTime;
        this.visible = visible;
        this.totalLike = totalLike;
        this.totalDislike = totalDislike;
        this.totalLove = totalLove;
        this.totalClap = totalClap;
        this.totalGrrr = totalGrrr;
        this.idMovie = idMovie;
    }

    public String getTextReview() {
        return textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitleMovie() {
        return titleMovie;
    }

    public void setTitleMovie(String titleMovie) {
        this.titleMovie = titleMovie;
    }

    public String getIdReview() {
        return idReview;
    }

    public void setIdReview(String idReview) {
        this.idReview = idReview;
    }

    public Date getDateAndTime() {
        return (Date)dateAndTime.clone();
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalDislike() {
        return totalDislike;
    }

    public void setTotalDislike(int totalDislike) {
        this.totalDislike = totalDislike;
    }

    public int getTotalLove() {
        return totalLove;
    }

    public void setTotalLove(int totalLove) {
        this.totalLove = totalLove;
    }

    public int getTotalClap() {
        return totalClap;
    }

    public void setTotalClap(int totalClap) {
        this.totalClap = totalClap;
    }

    public int getTotalGrrr() {
        return totalGrrr;
    }

    public void setTotalGrrr(int totalGrrr) {
        this.totalGrrr = totalGrrr;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    @Override
    public String toString() {
        return "Review{" +
                "textReview='" + textReview + '\'' +
                ", author='" + author + '\'' +
                ", titleMovie='" + titleMovie + '\'' +
                ", idReview='" + idReview + '\'' +
                ", dateAndTime=" + dateAndTime +
                ", visible=" + visible +
                ", totalLike=" + totalLike +
                ", totalDislike=" + totalDislike +
                ", totalLove=" + totalLove +
                ", totalClap=" + totalClap +
                ", totalGrrr=" + totalGrrr +
                ", idMovie=" + idMovie +
                '}';
    }
}
