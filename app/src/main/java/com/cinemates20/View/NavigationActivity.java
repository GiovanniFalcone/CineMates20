package com.cinemates20.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cinemates20.Presenter.NavigationPresenter;
import com.cinemates20.R;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class NavigationActivity extends AppCompatActivity{

    private NavigationPresenter navigationPresenter;
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

        navigationPresenter = new NavigationPresenter(this);

        MeowBottomNavigation navView = findViewById(R.id.bottom_nav_view);

        navView.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_movie_24));
        navView.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_search_24));
        navView.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_rss_feed_24));
        navView.add(new MeowBottomNavigation.Model(4, R.drawable.ic_baseline_notifications_black_24));
        navView.add(new MeowBottomNavigation.Model(5, R.drawable.ic_baseline_menu_24));

        navigationPresenter.isConnected();

        navigationPresenter.initUser();

        navigationPresenter.listerNotification(navView);

        navigationPresenter.updateLastLogin();

        //Fragment default
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, new HomeFragment());
        transaction.commit();

        navView.setOnShowListener(item -> {
            navigationPresenter.onClickNavigation(navView, item, mHomeFragment,
                    mSearchFragment, mFeedFragment, mNotificationFragment, mUserProfileFragment);
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
        // if there is a fragment and the back stack of this fragment is not empty,
        // then emulate 'onBackPressed' behaviour, because in default, it is not working
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }

        super.onBackPressed();
    }
}