package com.cinemates20.Model.DAO.Implements;

import android.util.Log;

import com.cinemates20.Model.DAO.Interface.Callbacks.NotificationCallback;
import com.cinemates20.Model.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.Model.Notification;
import com.cinemates20.Utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotificationDAO_Firestore implements NotificationDAO, NotificationCallback {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the users collection
    private final CollectionReference collectionReference = db.collection("notifications");

    @Override
    public void changeNotificationState(String currentUser) {
        collectionReference
                .whereEqualTo("userWhoReceived", currentUser)
                .whereEqualTo("flag", "unchecked")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                            String idDocument = documentSnapshot.getId();
                            collectionReference
                                    .document(idDocument).update("flag", "checked");
                        }
                    }else
                        Log.d("NotificationDAO", "Error getting documents: ", task.getException());
                });
    }

    @Override
    public void addRequest(String currentUser, String userWhoReceived) {
        //Create collection
        Map<String, Object> map = new HashMap<>();
        map.put("typeNotification", "RequestReceived");
        map.put("userWhoSent", currentUser);
        map.put("dateAndTime", new Timestamp(new Date()));
        map.put("userWhoReceived", userWhoReceived);
        map.put("flag", "unchecked");

        // Add a new document with a generated ID
        collectionReference
                .add(map)
                .addOnSuccessListener(documentReference -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public void removeRequestReceived(String currentUser, String userWhoReceivedRequest) {
        collectionReference
                .whereEqualTo("userWhoSent", currentUser)
                .whereEqualTo("userWhoReceived", userWhoReceivedRequest)
                .whereEqualTo("typeNotification", "RequestReceived")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                            String idRequest = documentSnapshot.getId();
                            collectionReference.document(idRequest).delete();
                        }
                    } else {
                        Log.d("NotificationDAO", "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void removeNotificationOfAcceptedRequest(String currentUser, String userWhoReceivedNotification) {
        //userWhoSent refers to the user who sent the notification of accepted friendship request

        collectionReference
                .whereEqualTo("userWhoSent", userWhoReceivedNotification)
                .whereEqualTo("typeNotification", "RequestAccepted")
                .get()
                .addOnCompleteListener(taskRequests -> {
                    if (taskRequests.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot1 : Objects.requireNonNull(taskRequests.getResult())){
                            String idRequests = documentSnapshot1.getId();
                            collectionReference
                                    .document(idRequests)
                                    .delete();
                        }
                    }else
                        Log.d("NotificationDAO", "Error getting documents: ", taskRequests.getException());
                });
    }

    @Override
    public List<String> getRequestReceived(String userWhoReceivedRequest, String currentUser) {
        List<String> listUsername = new ArrayList<>();

        // Create a query against the subcollection.
        Query queryRequest =  collectionReference
                .whereEqualTo("typeNotification", "RequestReceived")
                .whereEqualTo("userWhoReceived", currentUser)
                .orderBy("dateAndTime", Query.Direction.DESCENDING);

        //Get query results
        Task<QuerySnapshot> taskRequest = queryRequest.get().addOnCompleteListener(task2 -> {});
        Utils.waitTask(taskRequest);

        if(taskRequest.isSuccessful()) {
            for (DocumentSnapshot documentSnapshot2 : Objects.requireNonNull(taskRequest.getResult())) {
                listUsername.add(documentSnapshot2.getString("userWhoSent"));
            }
        } else
            Log.d("NotificationDAO", "Error getting documents: ", taskRequest.getException());

        return listUsername;
    }

    @Override
    public void sendNotificationAccepted(String currentUser, String userAdded, Timestamp dateAndTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("typeNotification", "RequestAccepted");
        map.put("userWhoSent", userAdded);
        map.put("dateAndTime", dateAndTime);
        map.put("userWhoReceived", currentUser);
        map.put("flag", "unchecked");

        // Add a new document with a generated ID
        collectionReference
                .add(map)
                .addOnSuccessListener(documentReference -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public List<Notification> getNotifications(String currentUser) {
        List<Notification> notificationList = new ArrayList<>();

        Query query = collectionReference
                .whereEqualTo("userWhoReceived", currentUser)
                .orderBy("dateAndTime", Query.Direction.DESCENDING);

        Task<QuerySnapshot> task = query.get().addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            for(DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                notificationList.add(documentSnapshot.toObject(Notification.class));
            }
        } else
            Log.d("NotificationDAO", "Error getting documents: ", task.getException());

        return notificationList;
    }

    @Override
    public List<Notification> updateNotifications(String currentUser, NotificationCallback notificationCallback) {
        List<Notification> notificationList = new ArrayList<>();

        collectionReference
                .whereIn("typeNotification", Arrays.asList("RequestReceived", "RequestAccepted", "report"))
                .whereEqualTo("userWhoReceived", currentUser)
                .whereEqualTo("flag", "unchecked")
                .addSnapshotListener((value, error) -> {
                    if (error != null)
                        Log.w("NotificationDao", "Listen failed.", error);
                    if (value != null) {
                        notificationCallback.numberNotification(value.size());
                    } else {
                        Log.d("NotificationDao", "Current data: null");
                    }
                });

        return notificationList;
    }
}
