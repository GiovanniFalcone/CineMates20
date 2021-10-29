package com.cinemates20.Model;

import java.util.Date;

public class Notification {

    private Date dateAndTime;
    private String type, flag;
    private String userWhoReceived, userWhoSent;

    public Date getDateAndTime() {
        return (Date)dateAndTime.clone();
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

    public void setUserWhoSent(String userWhoSent) {
        this.userWhoSent = userWhoSent;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "dateAndTime=" + dateAndTime +
                ", type='" + type + '\'' +
                ", flag='" + flag + '\'' +
                ", userWhoReceived='" + userWhoReceived + '\'' +
                ", userWhoSend='" + userWhoSent + '\'' +
                '}';
    }
}
