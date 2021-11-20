package com.cinemates20.Model.DAO.Interface.InterfaceDAO;

import com.cinemates20.Model.DAO.Interface.Callbacks.FeedCallback;
import com.google.firebase.Timestamp;

import java.util.List;

public interface FeedDAO {

    void addNews(String userOfTheNews, String secondUser, String movie, String idItemNews, String itemNewsType, float valuation, Timestamp dateAndTime);

    void getNews(List<String> friends, FeedCallback feedCallback);
}
