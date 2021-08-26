package com.cinemates20.DAO.Implements;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cinemates20.DAO.Interface.Callbacks.CommentCallback;
import com.cinemates20.Model.Comment;
import com.cinemates20.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CommentDAO_Firestore implements com.cinemates20.DAO.Interface.Firestore.CommentDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference commentRef = db.collection("comments");
    private Context context;
    private CommentCallback commentCallback;

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
    public List<Comment> getUserCommentByReview(String idReview) {
        List<Comment> commentList = new ArrayList<>();

        //Create task to get result and wait it
        Task<QuerySnapshot> task = commentRef.whereEqualTo("idReview", idReview)
                .orderBy("dateAndTime")
                .get().addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                commentList.add(documentSnapshot.toObject(Comment.class));
                Log.d("CommentDAO_Firestore", "data: " + commentList);
            }
        }
        else
            Log.d("CommentDAO_Firestore", "Error getting documents: ", task.getException());

        return commentList;
    }

    @Override
    public void getCommentByAuthor(String author, String titleMovie) {
        db.collection("reviews")
                .whereEqualTo("author", author)
                .whereEqualTo("titleMovie", titleMovie)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d("CommentDao", document.getId() + " => " + document.getData());
                                Comment comment = document.toObject(Comment.class);
                                commentCallback.setComment(comment);
                            }
                        } else {
                            Log.d("CommentDao", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }
}