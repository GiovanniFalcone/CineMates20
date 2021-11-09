package com.cinemates20.Model;

import java.util.Date;

public class Notification {

    private Date dateAndTime;
    private String typeNotification;
    private String userWhoSent, typeUser, typeItem;
    private boolean resultReport;

    public Notification() {}

    public Notification(Date dateAndTime, String typeNotification, String userWhoSent, String typeUser, String typeItem, boolean resultReport) {
        this.dateAndTime = dateAndTime;
        this.typeNotification = typeNotification;
        this.userWhoSent = userWhoSent;
        this.typeUser = typeUser;
        this.typeItem = typeItem;
        this.resultReport = resultReport;
    }

    public Date getDateAndTime() {
        return (Date)dateAndTime.clone();
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getTypeNotification() {
        return typeNotification;
    }

    public void setTypeNotification(String typeNotification) {
        this.typeNotification = typeNotification;
    }

    public String getUserWhoSent() {
        return userWhoSent;
    }

    public void setUserWhoSent(String userWhoSent) {
        this.userWhoSent = userWhoSent;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public String getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(String typeItem) {
        this.typeItem = typeItem;
    }

    public boolean isResultReport() {
        return resultReport;
    }

    public void setResultReport(boolean resultReport) {
        this.resultReport = resultReport;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "dateAndTime=" + dateAndTime +
                ", typeNotification='" + typeNotification + '\'' +
                ", userWhoSent='" + userWhoSent + '\'' +
                ", typeUser='" + typeUser + '\'' +
                ", typeItem='" + typeItem + '\'' +
                ", resultReport=" + resultReport +
                '}';
    }
}
