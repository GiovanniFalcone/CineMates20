package com.cinemates20.DAO.Implements;

import android.content.Context;
import android.util.Log;

import com.cinemates20.DAO.Interface.Firestore.MovieListDAO;
import com.cinemates20.Utils.Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class MovieListDAO_Firestore implements MovieListDAO {

    private final Context context;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the users collection
    private final CollectionReference collectionReference = db.collection("movieList");

    public MovieListDAO_Firestore(Context context) {
        this.context = context;
    }

    @Override
    public void addMovieToList(String currentUser, String listName, String idMovie) {
        //Create subcollection
        Map<String, Object> map = new HashMap<>();
        map.put("idMovie", idMovie);
        map.put("DateAndTime", new Timestamp(new Date()));
        map.put("listName", listName);
        map.put("username", currentUser);

        collectionReference
                .add(map)
                .addOnSuccessListener(documentReference -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public List<Integer> getMoviesByList(String clickedList, String currentUser) {
        List<Integer> moviesList = new ArrayList<>();

        Task <QuerySnapshot> taskSubCollection = collectionReference
                .whereEqualTo("listName", clickedList)
                .whereEqualTo("username", currentUser)
                .get()
                .addOnCompleteListener(task2 -> {});

        Utils.waitTask(taskSubCollection);

        if (taskSubCollection.isSuccessful()) {
            for (QueryDocumentSnapshot doc2 : Objects.requireNonNull(taskSubCollection.getResult())) {
                moviesList.add(Integer.parseInt(Objects.requireNonNull(doc2.getString("idMovie"))));
            }
        } else {
            Log.d("UserDAO", "Error getting documents: ", taskSubCollection.getException());
        }

        return moviesList;
    }

    @Override
    public List<String> getListsThatContainsCurrentMovie(String idMovie, String currentUser) {
        List<String> nameLists = new ArrayList<>();

        // Create a query against the subcollection.
        Query queryRequest =  collectionReference
                .whereEqualTo("idMovie", idMovie)
                .whereEqualTo("username", currentUser);

        //Get query results
        Task<QuerySnapshot> taskSub = queryRequest.get().addOnCompleteListener(task2 -> {});
        Utils.waitTask(taskSub);

        if (taskSub.isSuccessful()) {
            for (QueryDocumentSnapshot doc : Objects.requireNonNull(taskSub.getResult())) {
                nameLists.add(doc.getString("listName"));
            }
        } else {
            Log.d("UserDAO", "Error getting documents: ", taskSub.getException());
        }

        return nameLists;
    }

    @Override
    public void removeMovieFromList(String idMovie, String listName, String currentUser) {
        collectionReference
                .whereEqualTo("username", currentUser)
                .whereEqualTo("idMovie", idMovie)
                .whereEqualTo("listName", listName)
                .get()
                .addOnCompleteListener(task1 -> {
                    for(DocumentSnapshot doc1 : Objects.requireNonNull(task1.getResult())){
                        if(task1.isSuccessful()){
                            String idDocumentToDelete = doc1.getId();
                            //now we can delete the document
                            collectionReference
                                    .document(idDocumentToDelete)
                                    .delete();
                        }
                    }
                });
    }
}
