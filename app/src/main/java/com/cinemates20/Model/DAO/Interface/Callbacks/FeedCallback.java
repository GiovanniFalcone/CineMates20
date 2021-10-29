package com.cinemates20.Model.DAO.Interface.Callbacks;

import com.cinemates20.Model.Feed;

import java.util.List;

public interface FeedCallback {

    default void onSuccess(List<Feed> news) {}
}
