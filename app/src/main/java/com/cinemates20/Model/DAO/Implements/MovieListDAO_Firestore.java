package com.cinemates20.Model.DAO.Implements;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cinemates20.Model.DAO.Interface.InterfaceDAO.MovieListDAO;
import com.cinemates20.Model.MovieList;
import com.cinemates20.Utils.Utils;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MovieListDAO_Firestore implements MovieListDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the users collection
    private final CollectionReference collectionReference = db.collection("movieList");

    @Override
    public void addCustomList(String nameList, String description, String currentUser) {
        DocumentReference documentReference = collectionReference.document();

        // Create a new movie list
        Map<String, Object> map = new HashMap<>();
        map.put("idMovieList", documentReference.getId());
        map.put("username", currentUser);
        map.put("description", description);
        map.put("nameList", nameList);
        map.put("dateAndTime", new Timestamp(new Date()));
        map.put("listIDMovie", Collections.emptyList());

        // Save data into document
        documentReference.set(map)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public void addMovieToList(String currentUser, String listName, String idMovie) {
        collectionReference
                .whereEqualTo("nameList", listName)
                .whereEqualTo("username", currentUser)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            String idDocument = doc.getId();
                            collectionReference.document(idDocument)
                                    .update("listIDMovie", FieldValue.arrayUnion(Integer.valueOf(idMovie)));
                        }
                    }
                    else {
                        Log.d("MovieListDAO", "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public List<MovieList> getMovieListsNameByUser(String currentUser) {
        List<MovieList> nameList = new ArrayList<>();

        Task<QuerySnapshot> task = collectionReference
                .whereEqualTo("username", currentUser)
                .get()
                .addOnCompleteListener(task1 -> {});

        Utils.waitTask(task);

        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
               // nameList.add(doc.getString("nameList"));
                MovieList movieList = doc.toObject(MovieList.class);
                nameList.add(movieList);
            }
        } else {
            Log.d("MovieListDAO", "Error getting documents: ", task.getException());
        }

        return nameList;
    }
    
    @Override
    public List<String> getListsThatContainsCurrentMovie(String idMovie, String currentUser) {
        List<String> nameLists = new ArrayList<>();

        // Create a query against the collection.
        Query queryRequest =  collectionReference
                .whereArrayContains("listIDMovie", Integer.valueOf(idMovie))
                .whereEqualTo("username", currentUser);

        //Get query results
        Task<QuerySnapshot> taskSub = queryRequest.get().addOnCompleteListener(task2 -> {});
        Utils.waitTask(taskSub);

        if (taskSub.isSuccessful()) {
            for (QueryDocumentSnapshot doc : Objects.requireNonNull(taskSub.getResult())) {
                nameLists.add(doc.getString("nameList"));
            }
        } else {
            Log.d("MovieListDAO", "Error getting documents: ", taskSub.getException());
        }

        return nameLists;
    }

    @Override
    public void removeMovieFromList(String idMovie, String listName, String currentUser) {
        collectionReference
                .whereEqualTo("nameList", listName)
                .whereEqualTo("username", currentUser)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            String idDocument = doc.getId();
                            collectionReference.document(idDocument)
                                    .update("listIDMovie", FieldValue.arrayRemove(Integer.valueOf(idMovie)));
                        }
                    }
                    else {
                        Log.d("MovieListDAO", "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public boolean checkIfListAlreadyExists(String newNameList, String username) {
        Task<QuerySnapshot> task = collectionReference
                .whereEqualTo("nameList", newNameList)
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            return !task.getResult().isEmpty();
        } else
            Log.d("MovieListDAO", "Error getting documents: ", task.getException());

        return false;
    }
}
