package com.cinemates20.Model.DAO.Implements;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.cinemates20.Model.DAO.Interface.Callbacks.CommentCallback;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.CommentDAO;
import com.cinemates20.Model.Comment;
import com.cinemates20.Utils.Utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommentDAO_Firestore implements CommentDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference commentRef = db.collection("comments");

    @Override
    public void saveComment(String idReview, String author, String comment, Timestamp dateAndTime) {
        // Add a new document with a generated ID
        DocumentReference documentReference = commentRef.document();

        // Create a new comment this author, text and title of the movie reviewed
        Map<String, Object> map = new HashMap<>();
        map.put("idComment", documentReference.getId());
        map.put("idReview", idReview);
        map.put("author", author);
        map.put("textComment", comment);
        map.put("visible", true);
        map.put("counterForSpoiler", 0);
        map.put("counterForLanguage", 0);
        map.put("dateAndTime", dateAndTime);

        // Save data into document
        documentReference.set(map)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public void getUserCommentByReview(String idReview, Context context, CommentCallback commentCallback) {
        commentRef
                .whereEqualTo("idReview", idReview)
                .orderBy("dateAndTime")
                .addSnapshotListener((Activity) context, (value, error) -> {
                    if (error != null)
                        Log.w("UserDao", "Listen failed.", error);

                    for (DocumentChange dc : Objects.requireNonNull(value).getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                Comment comment = dc.getDocument().toObject(Comment.class);
                                commentCallback.setNewComments(comment);
                                Log.d("CommentDAO_Firestore", "new: " + dc.getDocument().getData());
                                break;
                            case MODIFIED:
                                Log.d("CommentDAO_Firestore", "modified: " + dc.getDocument().getData());
                                break;
                            case REMOVED:
                                Log.d("CommentDAO_Firestore", "removed: " + dc.getDocument().getData());
                                break;
                        }
                    }
                });
    }

    @Override
    public void updateCounter(String idComment, String reportType) {
        DocumentReference documentReference = commentRef.document(idComment);
        switch (reportType) {
            case "spoiler":
                documentReference.update("counterForSpoiler", FieldValue.increment(1));
                break;
            case "language":
                documentReference.update("counterForLanguage", FieldValue.increment(1));
                break;
        }
    }

    @Override
    public void changeState(String idComment, String reportType) {
        DocumentReference documentReference = commentRef.document(idComment);
        if ("language".equals(reportType)) {
            documentReference.update("visible", false);
        }
    }

    @Override
    public Number getCounter(String idComment, String reportType) {
        Task<DocumentSnapshot> task = commentRef.document(idComment).get().addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if (task.isSuccessful()) {
            DocumentSnapshot documentSnapshot = task.getResult();
            if(Objects.requireNonNull(documentSnapshot).exists()){
                switch (reportType){
                    case "spoiler":
                        return (Number) documentSnapshot.get("counterForSpoiler");
                    case "language":
                        return (Number) documentSnapshot.get("counterForLanguage");
                }
            }
        }

        return 0;
    }

}