package com.cinemates20.Presenter;

import android.net.Uri;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.View.UserProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UserProfilePresenter {

    private UserProfileFragment userProfileFragment;
    private UserDAO userDAO;

    public UserProfilePresenter(UserProfileFragment userProfileFragment){
        this.userProfileFragment = userProfileFragment;
    }

    public void onClickProPic(){
        Uri imageUri = userProfileFragment.getImageUri();
        userDAO = new UserDAO_Firestore(userProfileFragment.getContext());
        String currentUsername = userDAO.getUsername(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).getUsername();
        Uri previousUri = userDAO.getImageUri(currentUsername);

        if (!previousUri.toString().equals(""))
            userDAO.deletePicture(previousUri);

        userDAO.uploadPicture(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), imageUri);
        userProfileFragment.setPropic(imageUri);
    }

    public void setProfilePicture() {
        userDAO = new UserDAO_Firestore(userProfileFragment.getContext());
        String currentUsername = userDAO.getUsername(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).getUsername();
        Uri uri = userDAO.getImageUri(currentUsername);
        if (uri != null) {
            userProfileFragment.setPropic(uri);
        }

    }
}
