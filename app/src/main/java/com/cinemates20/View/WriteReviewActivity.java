package com.cinemates20.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.cinemates20.Presenter.WriteReviewPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class WriteReviewActivity extends AppCompatActivity {

    private EditText textBoxReview;
    private WriteReviewPresenter writeReviewPresenter;
    private String title, url, overview;
    private int idMovie;
    private ImageView poster;
    private TextView titleMovie, overviewMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        writeReviewPresenter = new WriteReviewPresenter(this);

        titleMovie = findViewById(R.id.movieTitle);
        overviewMovie = findViewById(R.id.movieOverview);
        poster = findViewById(R.id.moviePoster);
        textBoxReview = findViewById(R.id.editTextBoxReview);

        idMovie = getIntent().getIntExtra("MovieID", 0);
        title = getIntent().getStringExtra("MovieTitle");
        url = getIntent().getStringExtra("MovieUrl");
        overview = getIntent().getStringExtra("MovieOverview");

        titleMovie.setText(title);
        Glide.with(WriteReviewActivity.this.getApplicationContext())
                .load("http://image.tmdb.org/t/p/original"+ url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);

        Button buttonConfirmReview = findViewById(R.id.button2);
        buttonConfirmReview.setEnabled(false);

        textBoxReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonConfirmReview.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(textBoxReview.getText().toString().isEmpty())
                    buttonConfirmReview.setEnabled(false);
            }
        });

        buttonConfirmReview.setOnClickListener(view -> writeReviewPresenter.clickAddReview());
    }

    public String getMovieTitle(){
        return title;
    }

    public int getIdMovie(){
        return idMovie;
    }

    public String getCurrentUser(){
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    }

    public String getReviewText(){
        return textBoxReview.getText().toString().trim();
    }

    public Context getActivityContext(){
        return this;
    }
}