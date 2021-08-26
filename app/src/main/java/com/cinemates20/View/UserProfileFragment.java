package com.cinemates20.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cinemates20.Presenter.UserProfilePresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment {

    private ImageView userIcon;
    private Uri imageUri;
    private UserProfilePresenter userProfilePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        userProfilePresenter = new UserProfilePresenter(this);
        userIcon = view.findViewById(R.id.userIcon);

        userProfilePresenter.setProfilePicture();

        userIcon.setOnClickListener(view1 -> {
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                if(requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 1);
                }else
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);                }
        });
        return view;
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
            userProfilePresenter.onClickProPic();
        }
    }

    public void setPropic(Uri uri){
        Picasso.get().load(uri).transform(new CircleTransform()).into(userIcon);
    }

    public Uri getImageUri(){
        return imageUri;
    }
}