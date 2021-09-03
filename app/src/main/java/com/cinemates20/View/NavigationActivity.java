package com.cinemates20.View;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cinemates20.DAO.Implements.NotificationDAO_Firestore;
import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Callbacks.NotificationCallback;
import com.cinemates20.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.FeedFragment;
import com.cinemates20.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class NavigationActivity extends AppCompatActivity{

    private Fragment mHomeFragment = new HomeFragment();
    private Fragment mSearchFragment = new SearchFragment();
    private Fragment mFeedFragment = new FeedFragment();
    private Fragment mNotificationFragment = new NotificationFragment();
    private Fragment mUserProfileFragment = new MenuFragment();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);

        UserDAO_Firestore userDAO = new UserDAO_Firestore(getApplicationContext());
        NotificationDAO_Firestore notificationDAO = new NotificationDAO_Firestore(getApplicationContext());
        String currentUser = userDAO.getUsername(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).getUsername();
        notificationDAO.updateNotifica(currentUser, new NotificationCallback() {
            @Override
            public void numberNotification(int n) {
                if (n > 0)
                    navView.getOrCreateBadge(R.id.navigation_notification).setNumber(n);
                else
                    navView.removeBadge(R.id.navigation_notification);
            }
        });


        //Fragment default
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, new HomeFragment());
        transaction.commit();

        navView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.navigation_home:
                    fragment = mHomeFragment;
                    break;
                case R.id.navigation_search:
                    fragment = mSearchFragment;
                    break;
                case R.id.navigation_feed:
                    fragment = mFeedFragment;
                    break;
                case R.id.navigation_notification:
                    navView.removeBadge(R.id.navigation_notification);
                    fragment = mNotificationFragment;
                    break;
                case R.id.navigation_menu:
                    fragment = mUserProfileFragment;
                    break;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, Objects.requireNonNull(fragment))
                    .commit();
            return true;
        });

    }
}