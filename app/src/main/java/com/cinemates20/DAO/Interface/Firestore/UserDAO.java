package com.cinemates20.DAO.Interface.Firestore;

import android.net.Uri;

import com.cinemates20.Model.User;

import java.util.List;

public interface UserDAO {

    void saveUser(String username, String email);

    List<User> getListUsername(String usernameSearched, String currentUser);

    boolean checkIfUsernameExists(String username);

    User getUsername(String currentUser);

    Uri getImageUri(String currentUser);

    void deletePicture(Uri previousImageUri);

    void uploadPicture(String currentUser, Uri imageUri);

    void checkIfEmailExists_Firestore(String email);

    void addRequestSent(String currentUser, String userWhoReceivedRequest);

    void removeRequestSent(String currentUser, String userWhoReceivedRequest);

    void removeFriend(String currentUser, String friendToRemove);

    List<String> getFriends(String currentUser);

    List<String> getRequestSent(String userWhoRecivedRequest);

    void addFriend(String currentUser, String userWhoSentRequest);

    void addMovieToList(String currentUser, String listName, String idMovie);

    List<String> getMovieListsNameByUser(String currentUser);

    List<Integer> getMoviesByList(String clickedList, String currentUser);

    void addCustomList(String nameList, String currentUser);

    List<String> getListsThatContainsCurrentMovie(String idMovie, String currentUser);

    void removeMovieFromList(String movieTitle, String listName, String currentUser);
}
