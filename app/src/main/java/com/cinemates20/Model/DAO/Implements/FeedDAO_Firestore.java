package com.cinemates20.Model.DAO.Implements;

import android.util.Log;

import com.cinemates20.Model.DAO.Interface.Callbacks.FeedCallback;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.FeedDAO;
import com.cinemates20.Model.Feed;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FeedDAO_Firestore implements FeedDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference feedCollection = db.collection("feed");

    @Override
    public void addNews(String userOfTheNews, String secondUser, String movie, String idItemNews, String itemNewsType, float valuation, Timestamp dateAndTime) {
        // Add a new document with a generated ID
        DocumentReference documentReference = feedCollection.document();

        Log.d("FeedDao", "here new doc: " + documentReference.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("idFeed", documentReference.getId());
        map.put("userOfTheNews", userOfTheNews);
        map.put("secondUser", secondUser);
        map.put("movie", movie);
        map.put("valuation", valuation);
        map.put("idItemNews", idItemNews);
        map.put("itemNewsType", itemNewsType);
        map.put("dateAndTime", dateAndTime);

        documentReference.set(map)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public void getNews(List<String> friends, FeedCallback feedCallback) {
        List<Feed> news = new ArrayList<>();

        feedCollection
                .whereIn("userOfTheNews", friends)
                .orderBy("dateAndTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Feed feed = document.toObject(Feed.class);
                            news.add(feed);

                            Log.d("FeedFragment", "news: " + news);
                        }
                        feedCallback.onSuccess(news);
                    } else
                        Log.d("FeedDAO", "Error getting documents: ", task.getException());
                });
    }
}
