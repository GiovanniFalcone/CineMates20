package com.cinemates20.Presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.AuthenticationActivity;
import com.cinemates20.View.FriendListFragment;
import com.cinemates20.View.MenuFragment;

import com.cinemates20.View.MyListsFragment;
import com.cinemates20.View.MyReviewsFragment;
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

        if(!currentUser.getIcon().equals("")) {
            menuFragment.setPropic(currentUser.getIcon());
            menuFragment.setImageUri(Uri.parse(currentUser.getIcon()));
        }

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
        User.setCurrentUser(null);
        menuFragment.startActivity(new Intent(menuFragment.getContext(), AuthenticationActivity.class));
        menuFragment.requireActivity().finishAffinity();
    }

    public void clickButtonFriendList() {
        Utils.changeFragment_SlideAnim(menuFragment, new FriendListFragment(), R.id.container);
    }

    public void clickButtonMyReviews() {
        MyReviewsFragment myReviewsFragment = new MyReviewsFragment();
        if(menuFragment.getImageUri() != null) {
            Bundle args = new Bundle();
            args.putString("Icon", menuFragment.getImageUri().toString());
            myReviewsFragment.setArguments(args);
        }
        Utils.changeFragment_SlideAnim(menuFragment, myReviewsFragment, R.id.container);
    }

    public void clickButtonMovieList() {
        Utils.changeFragment_SlideAnim(menuFragment, new MyListsFragment(), R.id.container);
    }
}
