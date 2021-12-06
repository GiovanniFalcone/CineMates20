package com.cinemates20.Model.DAO.Interface.InterfaceDAO;

import android.net.Uri;

import com.cinemates20.Model.DAO.Interface.Callbacks.UserCallback;
import com.cinemates20.Model.User;

import java.util.List;

public interface UserDAO {

    void saveUser(String username, String email);

    List<User> getListUsername(String usernameSearched, String currentUser);

    boolean checkIfUsernameExists(String username);

    User getUser(String currentUser);

    Uri getImageUri(String currentUser);

    void deletePicture(Uri previousImageUri);

    void uploadPicture(String currentUser, Uri imageUri);

    void checkIfEmailExists_Firestore(String email, UserCallback userCallback);

    void removeFriend(String currentUser, String friendToRemove);

    List<String> getFriends(String currentUser);

    void addFriend(String currentUser, String userWhoSentRequest);

    void updateLastLogin(String currentUser);
}
