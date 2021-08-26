package com.cinemates20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.airbnb.lottie.LottieAnimationView;
import com.cinemates20.View.AuthenticationActivity;
import com.cinemates20.View.NavigationActivity;
import com.google.firebase.auth.FirebaseAuth;

public class OpeningActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        mAuth = FirebaseAuth.getInstance();

        lottieAnimationView = findViewById(R.id.animation);
        if(mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(OpeningActivity.this, NavigationActivity.class));
            finish();
        } else {
            animationLottie();
        }
    }

    private void animationLottie() {
        lottieAnimationView.animate().translationX(-1600).setDuration(1000).setStartDelay(4000);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(OpeningActivity.this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        },5000);
    }

}



