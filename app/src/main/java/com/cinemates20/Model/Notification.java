package com.cinemates20.Model;

import java.util.Date;

public class Notification {

    private Date dateAndTime;
    private String type;
    private String userWhoReceived, userWhoSent;

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserWhoReceived() {
        return userWhoReceived;
    }

    public void setUserWhoReceived(String userWhoReceived) {
        this.userWhoReceived = userWhoReceived;
    }

    public String getUserWhoSent() {
        return userWhoSent;
    }

    public void setUserWhoSend(String userWhoSend) {
        this.userWhoSent = userWhoSent;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "dateAndTime=" + dateAndTime +
                ", type='" + type + '\'' +
                ", userWhoReceived='" + userWhoReceived + '\'' +
                ", userWhoSend='" + userWhoSent + '\'' +
                '}';
    }
}
