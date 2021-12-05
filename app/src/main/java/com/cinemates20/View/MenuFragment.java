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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.cinemates20.Presenter.MenuPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;

public class MenuFragment extends Fragment {

    private Uri imageUri;
    private ImageView proPic;
    private TextView txtUsername;
    private CardView logout, myReviews, friends, myLists;
    private MenuPresenter menuPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        proPic = view.findViewById(R.id.userPropic);
        txtUsername = view.findViewById(R.id.txtUsername);
        logout = view.findViewById(R.id.exitCardView);
        friends = view.findViewById(R.id.friendListCardView);
        myReviews = view.findViewById(R.id.myReviewsCardView);
        myLists = view.findViewById(R.id.myListsCardView);

        menuPresenter = new MenuPresenter(this);
        menuPresenter.setMenu();

        onClickListener();

        return view;
    }

    public void onClickListener() {
        logout.setOnClickListener(view1 -> menuPresenter.clickButtonLogout());

        friends.setOnClickListener(view -> menuPresenter.clickButtonFriendList());

        myReviews.setOnClickListener(view -> menuPresenter.clickButtonMyReviews());

        myLists.setOnClickListener(view -> menuPresenter.clickButtonMovieList());

        proPic.setOnClickListener(view -> {
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                if(requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 1);
                }else
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        });
    }

    public void setUsername(String username){
        txtUsername.setText(String.format("Welcome %s!", username));
    }

    public void setPropic(String uri){
        Glide.with(requireContext().getApplicationContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleCrop())
                .into(proPic);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
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
            menuPresenter.onClickProPic(imageUri);
        }
    }
}