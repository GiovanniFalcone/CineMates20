package com.cinemates20.Model.DAO.Implements;

import android.net.Uri;
import android.util.Log;

import com.cinemates20.Model.DAO.Interface.Callbacks.UserCallback;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.Utils.Utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UserDAO_Firestore implements UserDAO {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the users collection
    private final CollectionReference collectionReference = db.collection("users");
    private boolean result;

    @Override
    public void saveUser(String username, String email) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("username", username.toLowerCase());
        user.put("email", email.toLowerCase());
        user.put("friends", Collections.emptyList());
        user.put("icon", "");
        user.put("last_login", new Timestamp(new Date()));
        user.put("first_login", new Timestamp(new Date()));

        // Add a new document with a generated ID
        collectionReference
                .add(user)
                .addOnSuccessListener(documentReference -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));

        createDefaultMovieList(username);
    }

    private void createDefaultMovieList(String username){
        DocumentReference documentReferenceFavorite = db.collection("movieList").document();

        Map<String, Object> favorite = new HashMap<>();
        favorite.put("idMovieList", documentReferenceFavorite.getId());
        favorite.put("username", username);
        favorite.put("description", "Your favorite list.");
        favorite.put("nameList", "Favorite list");
        favorite.put("dateAndTimeCreationList", new Timestamp(new Date()));
        favorite.put("listIDMovie", Collections.emptyList());

        // Save data into document
        documentReferenceFavorite.set(favorite)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReferenceFavorite.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));

        DocumentReference documentReferenceWatch = db.collection("movieList").document();

        Map<String, Object> watch = new HashMap<>();
        watch.put("idMovieList", documentReferenceWatch.getId());
        watch.put("username", username);
        watch.put("description", "Your watch list.");
        watch.put("nameList", "Watch list");
        watch.put("dateAndTimeCreationList", new Timestamp(new Date()));
        watch.put("listIDMovie", Collections.emptyList());

        // Save data into document
        documentReferenceWatch.set(watch)
                .addOnSuccessListener(unused -> Log.d("saveData", "DocumentSnapshot added with ID: " + documentReferenceWatch.getId()))
                .addOnFailureListener(e -> Log.w("saveData", "Error adding document", e));
    }

    @Override
    public List<User> getListUsername(String usernameSearched, String currentUser) {
        List<User> listUser = new ArrayList<>();

        // Create a query against the collection.
        Query query = collectionReference.orderBy("username")
                .startAt(usernameSearched.toLowerCase())
                .endBefore(usernameSearched.toLowerCase() + '~')
                .whereNotEqualTo("username", currentUser)
                .whereGreaterThanOrEqualTo("username", usernameSearched);

        //Create task and wait until it finish
        Task<QuerySnapshot> task  = query.get().addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful()){
            for (QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                User user = doc.toObject(User.class);
                listUser.add(user);
            }
        }

        return listUser;
    }

    @Override
    public User getUser(String currentUser) {
        User user = new User();

        Task <QuerySnapshot> task = collectionReference
                .whereEqualTo("email", currentUser)
                .get()
                .addOnCompleteListener(task1 -> {});

        Utils.waitTask(task);

        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult()))
                user = doc.toObject(User.class);
        } else {
            Log.d("UserDAO", "Error getting documents: ", task.getException());
        }

        return user;
    }

    @Override
    public Uri getImageUri(String currentUser) {
        Uri imageUri = null;
        Task<QuerySnapshot> task = collectionReference
                .whereEqualTo("username", currentUser)
                .get()
                .addOnCompleteListener(task1 -> {});

        Utils.waitTask(task);

        if(task.isSuccessful()){
            for(DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                imageUri = Uri.parse(documentSnapshot.getString("icon"));
            }
        }
        return imageUri;
    }

    @Override
    public void deletePicture(Uri previousImageUri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(String.valueOf(previousImageUri));
        storageReference.delete()
                .addOnSuccessListener(aVoid -> Log.d("msg", "onSuccess: deleted file"))
                .addOnFailureListener(exception -> Log.d("msg", "onFailure: did not delete file"));
    }

    @Override
    public void uploadPicture(String currentUser, Uri imageUri) {
        final String randomKey = UUID.randomUUID().toString();

        //Create a reference to image
        StorageReference documentRef = FirebaseStorage.getInstance().getReference().child("images/"+ randomKey);

        documentRef
                .putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> documentRef.getDownloadUrl().addOnSuccessListener(uri -> {

                    //Load uri into user document
                    collectionReference
                            .whereEqualTo("username", currentUser)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                                        String idUserDocument = documentSnapshot.getId();
                                        collectionReference
                                                .document(idUserDocument)
                                                .update("icon", uri.toString());
                                    }
                                }
                            });
                }));
    }

    @Override
    public void checkIfEmailExists_Firestore(String email, UserCallback userCallback) {
        // Add the listener callback
        collectionReference
                .whereEqualTo("email", email).get()
                .addOnCompleteListener(task1 -> {
                    // Check if our task successfully communicated with the server
                    if(task1.isSuccessful())
                        userCallback.isExists(!Objects.requireNonNull(task1.getResult()).isEmpty());
                });
    }

    @Override
    public void addRequestSent(String currentUser, String userWhoReceivedRequest) {
        Map<String, Object> map = new HashMap<>();
        map.put("friendshipRequestSentTo", userWhoReceivedRequest);
        map.put("DateAndTime", new Timestamp(new Date()));

        //Insert of the request in the subcollection of the requests received
        collectionReference
                .whereEqualTo("username", currentUser)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                            String idUserDocument = documentSnapshot.getId();
                            collectionReference
                                    .document(idUserDocument)
                                    .collection("RequestsSent")
                                    .document()
                                    .set(map);
                        }
                    }else
                        Log.d("UserDAO", "Error getting documents: ", task.getException());
                });
    }

    @Override
    public void addFriend(String currentUser, String userWhoSentRequest) {
        collectionReference
                .whereEqualTo("username", currentUser)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                            String idUserDocument = documentSnapshot.getId();
                            collectionReference
                                    .document(idUserDocument)
                                    .update("friends", FieldValue.arrayUnion(userWhoSentRequest));
                        }
                    }
                });
    }

    @Override
    public void updateLastLogin(String currentUser) {
        collectionReference.whereEqualTo("username", currentUser).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                            String idUserDocument = documentSnapshot.getId();
                            collectionReference
                                    .document(idUserDocument)
                                    .update("last_login", new Timestamp(new Date()));
                        }
                    } else {
                        Log.d("UserDAO", "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void removeRequestSent(String currentUser, String userWhoReceivedRequest) {
        //Remove from the request table of the user that received the request
        //get IdDocument of the user who received the friendship request
        collectionReference
                .whereEqualTo("username", currentUser)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                    String idUsers = documentSnapshot.getId();
                    //get IdDocument of the subcollection which conteines the user who send the request
                    collectionReference
                            .document(idUsers)
                            .collection("RequestsSent")
                            .whereEqualTo("friendshipRequestSentTo", userWhoReceivedRequest)
                            .get()
                            .addOnCompleteListener(taskRequests -> {
                                if (taskRequests.isSuccessful()){
                                    for (DocumentSnapshot documentSnapshot1 : Objects.requireNonNull(taskRequests.getResult())){
                                        String idRequests = documentSnapshot1.getId();
                                        collectionReference
                                                .document(idUsers)
                                                .collection("RequestsSent")
                                                .document(idRequests)
                                                .delete();
                                    }
                                }else
                                    Log.d("UserDAO", "Error getting documents: ", taskRequests.getException());
                            });
                }
            }else
                Log.d("UserDAO", "Error getting documents: ", task.getException());
        });
    }

    @Override
    public void removeFriend(String currentUser, String friendToRemove) {
        collectionReference
                .whereEqualTo("username", currentUser)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                            collectionReference
                                    .document(documentSnapshot.getId())
                                    .update("friends", FieldValue.arrayRemove(friendToRemove));
                        }
                    }
                });
    }

    @Override
    public List<String> getFriends(String currentUser) {
        User user = new User();

        Task <QuerySnapshot> task = collectionReference
                .whereEqualTo("username", currentUser)
                .get()
                .addOnCompleteListener(task1 -> {});

        Utils.waitTask(task);

        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                user = doc.toObject(User.class);
            }
        } else {
            Log.d("UserDAO", "Error getting documents: ", task.getException());
        }

        return user.getFriends();
    }

    @Override
    public List<String> getRequestSent(String userWhoReceivedRequest) {
        List<String> listUsername = new ArrayList<>();
        String idUser = getUserIdDocument();

        // Create a query against the subcollection.
        Query queryRequest =  collectionReference
                .document(idUser)
                .collection("RequestsSent")
                .orderBy("friendshipRequestSentTo")
                .startAt(userWhoReceivedRequest.toLowerCase())
                .endBefore(userWhoReceivedRequest.toLowerCase() + '~')
                .whereGreaterThanOrEqualTo("friendshipRequestSentTo", userWhoReceivedRequest);

        //Get query results
        Task<QuerySnapshot> taskRequest = queryRequest.get().addOnCompleteListener(task2 -> {});
        Utils.waitTask(taskRequest);

        if(taskRequest.isSuccessful()) {
            for (DocumentSnapshot documentSnapshot2 : Objects.requireNonNull(taskRequest.getResult())) {
                listUsername.add(documentSnapshot2.getString("friendshipRequestSentTo"));
            }
        }

        return listUsername;
    }

    @Override
    public List<String> getAllRequestSent(String currentUser) {
        List<String> listUsername = new ArrayList<>();
        String idUser = getUserIdDocument();

        // Create a query against the subcollection.
        Query queryRequest =  collectionReference
                .document(idUser)
                .collection("RequestsSent");

        //Get query results
        Task<QuerySnapshot> taskRequest = queryRequest.get().addOnCompleteListener(task2 -> {});
        Utils.waitTask(taskRequest);

        if(taskRequest.isSuccessful()) {
            for (DocumentSnapshot documentSnapshot2 : Objects.requireNonNull(taskRequest.getResult())) {
                listUsername.add(documentSnapshot2.getString("friendshipRequestSentTo"));
            }
        }

        return listUsername;
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        // Create a query against the collection.
        Query query = collectionReference.whereEqualTo("username", username);

        //Get query results
        Task<QuerySnapshot> task = query.get().addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if(task.isSuccessful())
            result = !Objects.requireNonNull(task.getResult()).isEmpty();

        return result;
    }

    private String getUserIdDocument(){
        //Query against collection
        Query query = collectionReference.whereEqualTo("email",
                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        //Create task and wait until it finish
        Task<QuerySnapshot> task = query.get().addOnCompleteListener(task1 -> {});
        Utils.waitTask(task);

        if (task.isSuccessful()) {
            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                return documentSnapshot.getId();
            }
        }

        return null;
    }
}