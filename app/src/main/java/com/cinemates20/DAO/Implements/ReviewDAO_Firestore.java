package com.cinemates20.DAO.Implements;

import android.content.Context;
import android.util.Log;

import com.cinemates20.DAO.Interface.Callbacks.ReviewCallback;
import com.cinemates20.DAO.Interface.Firestore.ReviewDAO;
import com.cinemates20.Model.Review;
import com.cinemates20.Model.User;
import com.cinemates20.Utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ReviewDAO_Firestore implements ReviewDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reviewCollection = db.collection("reviews");
    private Context context;
    private Review review;
    private ReviewCallback reviewCallback;

    public ReviewDAO_Firestore(Context context){ this.context = context; }

    public void setReviewCallback(ReviewCallback reviewCallback){
        this.reviewCallback = reviewCallback;
    }

    @Override
    public void saveReview(String currentUser, String text, int idMovie, String titleMovie) {
        // Add a new document with a generated ID
        DocumentReference documentReference = reviewCollection.document();

        // Create a new review this author, text and title of the movie reviewed
        Map<String, Object> review = new HashMap<>();
        review.put("idReview", documentReference.getId());
        review.put("author", currentUser);
        review.put("textReview", text);
        review.put("idMovie", idMovie);
        review.put("titleMovie", titleMovie);
        review.put("visible", true);
        review.put("totalLike", 0);
        review.put("totalLislike", 0);
        review.put("totalLove", 0);
        review.put("totalGrrr", 0);
        review.put("totalClap", 0);
        review.put("dateAndTime", new Timestamp(new Date()));

        // Save data into document
        documentReference.set(review)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public List<Review> getUserReviewByMovie(String titleMovie) {
        List<Review> listAuthor = new ArrayList<>();
        Task<QuerySnapshot> task = db.collection("reviews")
                .whereEqualTo("titleMovie", titleMovie)
                .get()
                .addOnCompleteListener(task1 -> { });
        Utils.waitTask(task);
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                Log.d("ReviewDao", document.getId() + " => " + document.getData());
                Review author = document.toObject(Review.class);
                listAuthor.add(author);
            }
        } else {
            Log.d("ReviewDao", "Error getting documents: ", task.getException());
        }
        return listAuthor;
    }

    @Override
    public Review getReviewByAuthor(String author, String titleMovie) {
        Task<QuerySnapshot> task = reviewCollection
                .whereEqualTo("author", author)
                .whereEqualTo("titleMovie", titleMovie)
                .get()
                .addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                review = document.toObject(Review.class);
        }else
            Log.d("ReviewDao", "Error getting documents: ", task.getException());

        return review;
    }

    @Override
    public void addReaction(String idReview, String buttonType, String username) {
        //Create subcollection
        Map<String, Object> map = new HashMap<>();
        map.put("id_review", idReview);
        map.put("username", username);
        map.put("buttonType", buttonType);

        reviewCollection.document(idReview)
                .collection("feedback").document().set(map);

        DocumentReference feedbackRef = reviewCollection.document(idReview);
        switch (buttonType) {
            case "like":
                feedbackRef.update("like", FieldValue.increment(1));
                break;
            case "dislike":
                feedbackRef.update("dislike", FieldValue.increment(1));
                break;
            case "love":
                feedbackRef.update("love", FieldValue.increment(1));
                break;
            case "clap":
                feedbackRef.update("clap", FieldValue.increment(1));
                break;
            case "grrr":
                feedbackRef.update("grrr", FieldValue.increment(1));
                break;
        }
    }

    @Override
    public void removeReaction(String idReview, String buttonType, String username) {
        DocumentReference feedbackRef = reviewCollection.document(idReview);

        //Decrement total of reactionType into review collection
        switch (buttonType) {
            case "like":
                feedbackRef.update("like", FieldValue.increment(-1));
                break;
            case "dislike":
                feedbackRef.update("dislike", FieldValue.increment(-1));
                break;
            case "love":
                feedbackRef.update("love", FieldValue.increment(-1));
                break;
            case "clap":
                feedbackRef.update("clap", FieldValue.increment(-1));
                break;
            case "grrr":
                feedbackRef.update("grrr", FieldValue.increment(-1));
                break;
        }

        //delete document of feedback of reaction deselected
        reviewCollection
                .document(idReview)
                .collection("feedback")
                .whereEqualTo("username", username)
                .whereEqualTo("buttonType", buttonType)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                            String idFeedback = documentSnapshot.getId();
                            db.collection("reviews")
                                    .document(idReview)
                                    .collection("feedback")
                                    .document(idFeedback)
                                    .delete();
                        }
                    }else
                        Log.d("UserDAO", "Error getting documents: ", task.getException());
                });
    }

    @Override
    public String getReaction(String currentUser, String idReview) {
        AtomicReference<String> reactionType = new AtomicReference<>();

        Query query = reviewCollection.document(idReview)
                .collection("feedback")
                .whereEqualTo("username", currentUser);

        Task<QuerySnapshot> task = query.get().addOnCompleteListener(task12 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                reactionType.set((String) document.get("buttonType"));
                Log.d("Feedback", reactionType.get());
            }
        }
        else {
            Log.d("ReviewDao", "Error getting documents: ", task.getException());
        }

        return reactionType.get();
    }

    @Override
    public List<User> getListNumberReactions(String buttonType, String idReview) {
        List<User> feedbacks = new ArrayList<>();

        Query query = reviewCollection.document(idReview)
                .collection("feedback")
                .whereEqualTo("buttonType", buttonType);

        Task<QuerySnapshot> task = query.get().addOnCompleteListener(task12 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                Log.d("Feedback", document.getId() + " => " + document.getData());
                User user = document.toObject(User.class);
                feedbacks.add(user);
            }
        }
        else {
            Log.d("ReviewDao", "Error getting documents: ", task.getException());
        }

        return feedbacks;
    }

    @Override
    public List<Review> getListReviews(String currentUser) {
        List<Review> reviewsList = new ArrayList<>();

        // Create a query against the collection.
        Query query = reviewCollection
                .whereEqualTo("author", currentUser);

        Task<QuerySnapshot> task = query.get().addOnCompleteListener(task12 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                reviewsList.add(document.toObject(Review.class));
            }
        }
        else {
            Log.d("ReviewDao", "Error getting documents: ", task.getException());
        }

        return reviewsList;
    }

}
