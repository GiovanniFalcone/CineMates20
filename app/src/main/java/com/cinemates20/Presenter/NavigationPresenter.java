package com.cinemates20.Presenter;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Callbacks.NotificationCallback;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.NotificationDAO;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.NavigationActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class NavigationPresenter {

    private final NavigationActivity navigationActivity;

    public NavigationPresenter(NavigationActivity navigationActivity){
        this.navigationActivity = navigationActivity;
    }

    public void isConnected() {
        if(!Utils.isNetworkConnected(navigationActivity.getApplicationContext())) {
            Utils.showErrorDialog(navigationActivity.getApplicationContext(), "Network error", "Seems that there isn't stable connection to internet, try again.");
            Log.d("NavigationActivity", "No connection, app closed");
            navigationActivity.finish();
        }
    }

    public void initUser() {
        if(User.getCurrentUser() == null) {
            User.setCurrentUser(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }

        Log.d("CurrentUser", User.getCurrentUser());
    }

    public void listerNotification(MeowBottomNavigation navView) {
        NotificationDAO notificationDAO = DAOFactory.getNotificationDAO(DAOFactory.FIREBASE);
        String currentUser = User.getCurrentUser();
        notificationDAO.updateNotifications(currentUser,  new NotificationCallback() {
            @Override
            public void numberNotification(int n) {
                if (n > 0)
                    navView.setCount(4, String.valueOf(n));
                else
                    navView.clearCount(4);
            }
        });
    }

    public void updateLastLogin() {
        String currentUser = User.getCurrentUser();
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);
        userDAO.updateLastLogin(currentUser);
    }

    public void onClickNavigation(MeowBottomNavigation navView, MeowBottomNavigation.Model item,
                                  Fragment mHomeFragment, Fragment mSearchFragment,
                                  Fragment mFeedFragment, Fragment mNotificationFragment,
                                  Fragment mUserProfileFragment) {
        Fragment fragment = null;
        switch (item.getId()) {
            case 1:
                fragment = mHomeFragment;
                break;
            case 2:
                fragment = mSearchFragment;
                break;
            case 3:
                fragment = mFeedFragment;
                break;
            case 4:
                navView.clearCount(R.id.navigation_notification);
                fragment = mNotificationFragment;
                break;
            case 5:
                fragment = mUserProfileFragment;
                break;
        }
        //change fragment
        navigationActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, Objects.requireNonNull(fragment))
                .commit();
    }
}
