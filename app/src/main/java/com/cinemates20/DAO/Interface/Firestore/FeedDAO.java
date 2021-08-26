package com.cinemates20.DAO.Interface.Firestore;

import com.cinemates20.Model.Notification;
import com.cinemates20.Model.Review;

import java.util.List;

public interface FeedDAO {

    List<Review> getReviewList(List<String> friendList);
    List<Notification> getNewFriendship(List<String> friendList);
}
