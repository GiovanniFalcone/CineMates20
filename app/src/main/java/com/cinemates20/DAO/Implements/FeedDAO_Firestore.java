package com.cinemates20.DAO.Implements;

import android.content.Context;

import com.cinemates20.DAO.Interface.Firestore.FeedDAO;
import com.cinemates20.Model.Feed;
import com.cinemates20.Model.Notification;
import com.cinemates20.Model.Review;
import com.cinemates20.Utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedDAO_Firestore implements FeedDAO {

    private Context context;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FeedDAO_Firestore(Context context) {
        this.context = context;
    }

    @Override
    public List<Review> getReviewList(List<String> friendList) {
        Feed feed = new Feed();
        List<Review> reviewList = new ArrayList<>();

        Query query = db.collection("reviews")
                .whereIn("author", friendList)
                .orderBy("dateAndTime", Query.Direction.DESCENDING);

        Task<QuerySnapshot> task = query.get().addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                reviewList.add(documentSnapshot.toObject(Review.class));
                feed.setReviewList(reviewList);
            }
        }

        return reviewList;
    }

    @Override
    public List<Notification> getNewFriendship(List<String> friendList) {
        Feed feed = new Feed();
        List<Notification> requestAcceptedByFriend  = new ArrayList<>();

        Query query = db.collection("notifications")
                .whereIn("userWhoSent", friendList)
                .orderBy("dateAndTime", Query.Direction.DESCENDING);

        Task<QuerySnapshot> task = query.get().addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                requestAcceptedByFriend.add(documentSnapshot.toObject(Notification.class));
                feed.setRequestAcceptedList(requestAcceptedByFriend);
            }
        }

        return requestAcceptedByFriend;
    }




}
