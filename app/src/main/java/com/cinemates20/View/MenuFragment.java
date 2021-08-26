package com.cinemates20.View;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cinemates20.Presenter.MenuPresenter;
import com.cinemates20.Presenter.NavigationPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.CircleTransform;
import com.cinemates20.Utils.Utils;
import com.squareup.picasso.Picasso;

public class MenuFragment extends Fragment {

    private Uri imageUri;
    private ImageView proPic;
    private CardView logout, myReviews, friends, myLists;
    private NavigationPresenter navigationPresenter;
    private MenuPresenter menuPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        proPic = view.findViewById(R.id.userPropic);
        logout = view.findViewById(R.id.notificationCardView);
        friends = view.findViewById(R.id.friendListCardView);
        myReviews = view.findViewById(R.id.myReviewsCardView);
        myLists = view.findViewById(R.id.myListsCardView);

        navigationPresenter = new NavigationPresenter(this);
        menuPresenter = new MenuPresenter(this);
        menuPresenter.setMenu();

        onClickListener();

        return view;
    }

    public void onClickListener() {
        logout.setOnClickListener(view1 -> navigationPresenter.clickButtonLogout());

        friends.setOnClickListener(view ->
                Utils.changeFragment(MenuFragment.this, new FriendListFragment(), R.id.nav_host_fragment_activity_main));

        myReviews.setOnClickListener(view ->
                Utils.changeFragment(MenuFragment.this, new MyReviewsFragment(), R.id.nav_host_fragment_activity_main));

        myLists.setOnClickListener(view ->
                Utils.changeFragment(MenuFragment.this, new MyListsFragment(), R.id.nav_host_fragment_activity_main));

        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                    if(requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);
                    }else
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });
    }

    public void setPropic(String uri){
        Picasso.get().load(uri).transform(new CircleTransform()).into(proPic);
    }

    public Uri getImageUri(){
        return imageUri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }else
                Toast.makeText(getContext(),"Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            menuPresenter.onClickProPic();
        }
    }
}