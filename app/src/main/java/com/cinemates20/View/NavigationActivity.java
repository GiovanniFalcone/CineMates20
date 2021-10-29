package com.cinemates20.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Callbacks.NotificationCallback;
import com.cinemates20.Model.DAO.Interface.Firestore.NotificationDAO;
import com.cinemates20.R;

import com.cinemates20.Utils.Utils;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class NavigationActivity extends AppCompatActivity{

    private final Fragment mHomeFragment = new HomeFragment();
    private final Fragment mSearchFragment = new SearchFragment();
    private final Fragment mFeedFragment = new FeedFragment();
    private final Fragment mNotificationFragment = new NotificationFragment();
    private final Fragment mUserProfileFragment = new MenuFragment();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        MeowBottomNavigation navView = findViewById(R.id.bottom_nav_view);

        navView.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_movie_24));
        navView.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_search_24));
        navView.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_rss_feed_24));
        navView.add(new MeowBottomNavigation.Model(4, R.drawable.ic_baseline_notifications_black_24));
        navView.add(new MeowBottomNavigation.Model(5, R.drawable.ic_baseline_menu_24));

        if(!Utils.isNetworkConnected(this)) {
            Utils.showErrorDialog(this, "Network error", "Seems that there isn't stable connection to internet, try again.");
            Log.d("NavigationActivity", "No connection, app closed");
            finish();
        }

        Log.d("NavigationActivity", "Welcome " + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        notificationDAO.updateNotifications(currentUser,  new NotificationCallback() {
            @Override
            public void numberNotification(int n) {
                if (n > 0)
                    navView.setCount(4, String.valueOf(n));
                else
                    navView.clearCount(4);
            }
        });


        //Fragment default
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, new HomeFragment());
        transaction.commit();


        navView.setOnShowListener(item -> {
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
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, Objects.requireNonNull(fragment))
                    .commit();
        });

        navView.setOnClickMenuListener(item -> {

        });

        navView.setOnReselectListener(item -> {

        });

        navView.show(1, true);
    }

    /*This method will clear focus of edit text if user touch screen outside of editText*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( ev );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}