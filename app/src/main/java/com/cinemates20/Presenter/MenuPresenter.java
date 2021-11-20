package com.cinemates20.Presenter;

import android.content.Intent;
import android.net.Uri;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.AuthenticationActivity;
import com.cinemates20.View.MenuFragment;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MenuPresenter {

    private final MenuFragment menuFragment;

    public MenuPresenter (MenuFragment menuFragment){
        this.menuFragment = menuFragment;
    }

    /**
     * Get user data and set icon into the menu
     */
    public void setMenu() {
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);

        User currentUser = userDAO.getUser((Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));

        if(!currentUser.getIcon().equals(""))
            menuFragment.setPropic(currentUser.getIcon());

        menuFragment.setUsername(currentUser.getUsername());
    }

    /**
     * The user can change his icon.
     * After selecting an image from the gallery, a check is made to see if the user already has an icon.
     * Then the new image is loaded into the storage and set in the user's menu.
     * @param imageUri the image selected from gallery
     */
    public void onClickProPic(Uri imageUri) {
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);

        String currentUsername = User.getCurrentUser();
        Uri previousUri = userDAO.getImageUri(currentUsername);

        if (!previousUri.toString().equals(""))
            userDAO.deletePicture(previousUri);

        userDAO.uploadPicture(currentUsername, imageUri);
        menuFragment.setPropic(imageUri.toString());
    }

    public void clickButtonLogout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        menuFragment.startActivity(new Intent(menuFragment.getContext(), AuthenticationActivity.class));
    }
}
