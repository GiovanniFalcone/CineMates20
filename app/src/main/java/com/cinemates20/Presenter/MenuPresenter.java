package com.cinemates20.Presenter;

import android.net.Uri;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.View.MenuFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MenuPresenter {

    private MenuFragment menuFragment;
    private UserDAO userDAO;

    public MenuPresenter (MenuFragment menuFragment){
        this.menuFragment = menuFragment;
    }

    /**
     * Get user date and set icon into the menu
     */
    public void setMenu() {
        userDAO = new UserDAO_Firestore(menuFragment.getContext());
        User currentUser = userDAO.getUsername((Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));
        if(!currentUser.getIcon().equals(""))
            menuFragment.setPropic(currentUser.getIcon());
    }

    public void onClickProPic() {
        Uri imageUri = menuFragment.getImageUri();
        userDAO = new UserDAO_Firestore(menuFragment.getContext());
        String currentUsername = userDAO.getUsername(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).getUsername();
        Uri previousUri = userDAO.getImageUri(currentUsername);

        if (!previousUri.toString().equals(""))
            userDAO.deletePicture(previousUri);

        userDAO.uploadPicture(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), imageUri);
        menuFragment.setPropic(imageUri.toString());
    }
}
