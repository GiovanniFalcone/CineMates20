package com.cinemates20.Presenter;

import android.content.Intent;

import com.cinemates20.View.MenuFragment;
import com.cinemates20.View.AuthenticationActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationPresenter {

    private MenuFragment menuFragment;
    public NavigationPresenter(MenuFragment menuFragment){
        this.menuFragment = menuFragment;
    }

    public void clickButtonLogout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        menuFragment.startActivity(new Intent(menuFragment.getContext(), AuthenticationActivity.class));
    }

}
