package com.cinemates20.DAO.Implements;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.cinemates20.DAO.Interface.Callbacks.CommentCallback;
import com.cinemates20.DAO.Interface.Firestore.CommentDAO;
import com.cinemates20.Model.Comment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.ListenerRegistrationImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CommentDAO_Firestore implements CommentDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference commentRef = db.collection("comments");
    private Context context;
    private CommentCallback commentCallback;
    private ListenerRegistration listenerRegistration;

    public CommentDAO_Firestore(Context context){ this.context = context; }

    public void setCommentCallback(CommentCallback commentCallback){
        this.commentCallback = commentCallback;
    }

    @Override
    public void saveComment(String idReview, String author, String comment) {
        // Add a new document with a generated ID
        DocumentReference documentReference = commentRef.document();

        // Create a new comment this author, text and title of the movie reviewed
        Map<String, Object> map = new HashMap<>();
        map.put("idComment", documentReference.getId());
        map.put("idReview", idReview);
        map.put("author", author);
        map.put("textComment", comment);
        map.put("visible", true);
        map.put("dateAndTime", new Timestamp(new Date()));

        // Save data into document
        documentReference.set(map)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public List<Comment> getUserCommentByReview(String idReview, Context context) {
        List<Comment> commentList = new ArrayList<>();

        listenerRegistration = commentRef
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

        return commentList;
    }

    @Override
    public void getCommentByAuthor(String author, String titleMovie) {
        db.collection("reviews")
                .whereEqualTo("author", author)
                .whereEqualTo("titleMovie", titleMovie)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Log.d("CommentDao", document.getId() + " => " + document.getData());
                            Comment comment = document.toObject(Comment.class);
                            commentCallback.setComment(comment);
                        }
                    } else {
                        Log.d("CommentDao", "Error getting documents: ", task.getException());
                    }
                });
    }

}