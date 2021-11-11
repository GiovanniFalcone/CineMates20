package com.cinemates20.Model.DAO.Implements;

import android.util.Log;

import com.cinemates20.Model.DAO.Interface.Firestore.MovieListDAO;
import com.cinemates20.Model.MovieList;
import com.cinemates20.Utils.Utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the users collection
    private final CollectionReference collectionReference = db.collection("movieList");
    private final CollectionReference moviesReference = db.collection("movies");

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

        // Save data into document
        documentReference.set(map)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public void addMovieToList(String currentUser, String listName, String idMovie, Timestamp dateAndTime) {
        //Create sub collection
        Map<String, Object> map = new HashMap<>();
        map.put("idMovie", idMovie);
        map.put("listName", listName);
        map.put("user", currentUser);
        map.put("dateAndTime", dateAndTime);
        moviesReference
                .add(map)
                .addOnSuccessListener(documentReference -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
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
    public List<Integer> getMoviesByList(String nameList, String currentUser) {
        List<Integer> movieList = new ArrayList<>();

        Task <QuerySnapshot> task = moviesReference
                .whereEqualTo("listName", nameList)
                .whereEqualTo("user", currentUser)
                .get()
                .addOnCompleteListener(task2 -> {});

        Utils.waitTask(task);

        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc2 : Objects.requireNonNull(task.getResult())) {
                movieList.add(Integer.parseInt(Objects.requireNonNull(doc2.getString("idMovie"))));
            }
        } else {
            Log.d("MovieListDAO", "Error getting documents: ", task.getException());
        }

        return movieList;
    }

    @Override
    public List<String> getListsThatContainsCurrentMovie(String idMovie, String currentUser) {
        List<String> nameLists = new ArrayList<>();

        // Create a query against the subcollection.
        Query queryRequest =  moviesReference
                .whereEqualTo("idMovie", idMovie)
                .whereEqualTo("user", currentUser);

        //Get query results
        Task<QuerySnapshot> taskSub = queryRequest.get().addOnCompleteListener(task2 -> {});
        Utils.waitTask(taskSub);

        if (taskSub.isSuccessful()) {
            for (QueryDocumentSnapshot doc : Objects.requireNonNull(taskSub.getResult())) {
                nameLists.add(doc.getString("listName"));
            }
        } else {
            Log.d("MovieListDAO", "Error getting documents: ", taskSub.getException());
        }

        return nameLists;
    }

    @Override
    public void removeMovieFromList(String idMovie, String listName, String currentUser) {
        moviesReference
                .whereEqualTo("user", currentUser)
                .whereEqualTo("idMovie", idMovie)
                .whereEqualTo("listName", listName)
                .get()
                .addOnCompleteListener(task1 -> {
                    for(DocumentSnapshot doc1 : Objects.requireNonNull(task1.getResult())){
                        if(task1.isSuccessful()){
                            String idDocumentToDelete = doc1.getId();
                            //now we can delete the document
                            moviesReference
                                    .document(idDocumentToDelete)
                                    .delete();
                        }
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
