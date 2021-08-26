package com.cinemates20.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private RecyclerView recyclerView;

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
        Utils.putImage(WriteReviewActivity.this, url, poster);
        overviewMovie.setText(overview);
        Button buttonConfirmReview = findViewById(R.id.button2);

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