package com.cinemates20.Model;

import java.util.Date;

public class Feed {

    private String idFeed, userOfTheNews, secondUser, movie, idItemNews, itemNewsType;
    private Date dateAndTime;
    private float valuation;

    public Feed(){}

    public Feed(String idFeed, String userOfTheNews, String secondUser, String movie, String idItemNews, String itemNewsType, Date dateAndTime, float valuation) {
        this.idFeed = idFeed;
        this.userOfTheNews = userOfTheNews;
        this.secondUser = secondUser;
        this.movie = movie;
        this.idItemNews = idItemNews;
        this.itemNewsType = itemNewsType;
        this.dateAndTime = dateAndTime;
        this.valuation = valuation;
    }

    public String getIdFeed() {
        return idFeed;
    }

    public void setIdFeed(String idFeed) {
        this.idFeed = idFeed;
    }

    public String getUserOfTheNews() {
        return userOfTheNews;
    }

    public void setUserOfTheNews(String userOfTheNews) {
        this.userOfTheNews = userOfTheNews;
    }

    public String getIdItemNews() {
        return idItemNews;
    }

    public void setIdItemNews(String idItemNews) {
        this.idItemNews = idItemNews;
    }

    public String getItemNewsType() {
        return itemNewsType;
    }

    public void setItemNewsType(String itemNewsType) {
        this.itemNewsType = itemNewsType;
    }

    public Date getDateAndTime() {
        return (Date) dateAndTime.clone();
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(String secondUser) {
        this.secondUser = secondUser;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public float getValuation() {
        return valuation;
    }

    public void setValuation(float valuation) {
        this.valuation = valuation;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "idFeed='" + idFeed + '\'' +
                ", userOfTheNews='" + userOfTheNews + '\'' +
                ", secondUser='" + secondUser + '\'' +
                ", movie='" + movie + '\'' +
                ", idItemNews='" + idItemNews + '\'' +
                ", itemNewsType='" + itemNewsType + '\'' +
                ", dateAndTime=" + dateAndTime +
                ", valuation=" + valuation +
                '}';
    }
}
