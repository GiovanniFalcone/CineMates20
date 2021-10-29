package com.cinemates20.Model.DAO.Implements;

import android.util.Log;

import com.cinemates20.Model.DAO.Interface.Callbacks.ReviewCallback;
import com.cinemates20.Model.DAO.Interface.Firestore.ReviewDAO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ReviewDAO_Firestore implements ReviewDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reviewCollection = db.collection("reviews");

    @Override
    public void saveReview(String currentUser, float valuation, String text, int idMovie, String titleMovie, Timestamp dateAndTime, ReviewCallback reviewCallback) {
        // Add a new document with a generated ID
        DocumentReference documentReference = reviewCollection.document();

        // Create a new review this author, text and title of the movie reviewed
        Map<String, Object> review = new HashMap<>();
        review.put("idReview", documentReference.getId());
        review.put("author", currentUser);
        review.put("movieValuation", valuation);
        review.put("textReview", text);
        review.put("idMovie", idMovie);
        review.put("titleMovie", titleMovie);
        review.put("totalLike", 0);
        review.put("totalDislike", 0);
        review.put("totalLove", 0);
        review.put("totalGrrr", 0);
        review.put("totalClap", 0);
        review.put("totalValuation", 0);
        review.put("rating", 0);
        review.put("counterForSpoiler", 0);
        review.put("counterForLanguage", 0);
        review.put("isInappropriate", false);
        review.put("hasCommentWithSpoiler", false);
        review.put("dateAndTime", dateAndTime);

        // Save data into document
        documentReference.set(review)
                .addOnSuccessListener(unused -> {
                    Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId());
                    reviewCallback.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public void updateReview(String username, String textReview, int idMovie, Timestamp dateAndTime, ReviewCallback reviewCallback) {
        Task<QuerySnapshot> task = reviewCollection
                .whereEqualTo("author", username)
                .whereEqualTo("idMovie", idMovie)
                .get()
                .addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        String idDocument = null;

        if(task.isSuccessful()){
            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                idDocument = document.getId();
                reviewCollection.document(idDocument).update("textReview", textReview);
                reviewCollection.document(idDocument).update("dateAndTime", dateAndTime);
            }
            reviewCallback.onSuccess(idDocument);
        }
        else {
            Log.d("ReviewDao", "Error getting documents: ", task.getException());
        }
    }

    @Override
    public List<Review> getUserReviewByMovie(String titleMovie, List<String> friends, String username) {
        List<Review> listAuthor = new ArrayList<>();
        friends.add(username);

        Task<QuerySnapshot> task = db.collection("reviews")
                .whereEqualTo("titleMovie", titleMovie)
                .whereEqualTo("isInappropriate", false)
                .whereIn("author", friends)
                .whereNotEqualTo("textReview", "")
                .orderBy("textReview")
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
    public Review getReviewByAuthor(String author, int idMovie) {
        Review review = new Review();
        Task<QuerySnapshot> task = reviewCollection
                .whereEqualTo("author", author)
                .whereEqualTo("idMovie", idMovie)
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
    public Review getReviewById(String idReview) {
        Review review = new Review();

        Task<DocumentSnapshot> task = reviewCollection
                .document(idReview)
                .get()
                .addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if(Objects.requireNonNull(document).exists())
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
                feedbackRef.update("totalLike", FieldValue.increment(1));
                break;
            case "dislike":
                feedbackRef.update("totalDislike", FieldValue.increment(1));
                break;
            case "love":
                feedbackRef.update("totalLove", FieldValue.increment(1));
                break;
            case "clap":
                feedbackRef.update("totalClap", FieldValue.increment(1));
                break;
            case "grrr":
                feedbackRef.update("totalGrrr", FieldValue.increment(1));
                break;
        }
    }

    @Override
    public void removeReaction(String idReview, String buttonType, String username) {
        DocumentReference feedbackRef = reviewCollection.document(idReview);

        //Decrement total of reactionType into review collection
        switch (buttonType) {
            case "like":
                feedbackRef.update("totalLike", FieldValue.increment(-1));
                break;
            case "dislike":
                feedbackRef.update("totalDislike", FieldValue.increment(-1));
                break;
            case "love":
                feedbackRef.update("totalLove", FieldValue.increment(-1));
                break;
            case "clap":
                feedbackRef.update("totalClap", FieldValue.increment(-1));
                break;
            case "grrr":
                feedbackRef.update("totalGrrr", FieldValue.increment(-1));
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
                        Log.d("ReviewDAO", "Error getting documents: ", task.getException());
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

    @Override
    public void addValuationToUserReview(float valuation, String idReview, String username) {
        //Create subcollection
        Map<String, Object> map = new HashMap<>();
        map.put("id_review", idReview);
        map.put("username", username);
        map.put("valuation", valuation);

        reviewCollection.document(idReview)
                .collection("valuation").document().set(map);
    }

    @Override
    public boolean checkIfValuationExists(String idReview, String username) {
        boolean result = false;

        Task<QuerySnapshot> task = reviewCollection.document(idReview)
                .collection("valuation")
                .whereEqualTo("id_review", idReview)
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful())
            result = !Objects.requireNonNull(task.getResult()).isEmpty();
        else
            Log.d("ReviewDao", "Error getting documents: ", task.getException());

        return result;
    }

    @Override
    public void updateValuationToUserReview(float valuation, String idReview, String username) {
        Task<QuerySnapshot> task = reviewCollection.document(idReview)
                .collection("valuation")
                .whereEqualTo("username", username)
                .whereEqualTo("id_review", idReview)
                .get()
                .addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                String idDocument = document.getId();
                reviewCollection.document(idReview).collection("valuation")
                        .document(idDocument).update("valuation", valuation);
            }
        }
        else {
            Log.d("ReviewDao", "Error getting documents: ", task.getException());
        }
    }

    @Override
    public void updateRatingReview(float valuation, String idReview, String username, String state) {
        if(state.equals("new")) {
            reviewCollection.document(idReview).update("totalValuation", FieldValue.increment(1));

            reviewCollection.document(idReview)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(Objects.requireNonNull(documentSnapshot).exists()){
                                Number totalValuation = (Number) documentSnapshot.get("totalValuation");
                                Number rating = (Number) documentSnapshot.get("rating");

                                float result = 0;
                                if(rating != null && totalValuation != null)
                                    result = (rating.floatValue() * (totalValuation.intValue() - 1) + valuation)
                                            /totalValuation.intValue();

                                reviewCollection.document(idReview).update("rating", result);
                            }
                        }
                    });
        } else {
            reviewCollection.document(idReview).collection("valuation")
                    .whereEqualTo("id_review", idReview)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            float rating = 0f;
                            int counter = 0;
                            Number number;

                            for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                number = (Number) documentSnapshot.get("valuation");
                                if(number != null) {
                                    rating = rating + number.floatValue();
                                    counter++;
                                }
                            }

                            rating = rating/counter;

                            reviewCollection.document(idReview).update("rating", rating);
                        }
                    });
        }
    }

    @Override
    public void getMovieRating(int idMovie, ReviewCallback reviewCallback) {
        reviewCollection
                .whereEqualTo("idMovie", idMovie)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        float rating = 0f;
                        int counter = 0;
                        Number number;

                        for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                            number = (Number) documentSnapshot.get("movieValuation");
                            if(number != null) {
                                rating = rating + number.floatValue();
                                counter++;
                            }
                        }

                        rating = rating/counter;

                        reviewCallback.setRating(rating, counter);
                    }
                });
    }

    @Override
    public void updateCounter(String idReview, String reportType) {
        DocumentReference documentReference = reviewCollection.document(idReview);
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
    public void changeState(String idReview, String reportType) {
        DocumentReference documentReference = reviewCollection.document(idReview);
        if ("language".equals(reportType)) {
            documentReference.update("isInappropriate", true);
        }
    }

    @Override
    public void updateCommentFlag(String idComment, String idReview) {
        DocumentReference documentReference = reviewCollection.document(idReview);
        documentReference.update("hasCommentWithSpoiler", true);
    }
}
