package com.cinemates20.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.cinemates20.Presenter.WriteReviewPresenter;
import com.cinemates20.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WriteReviewActivity extends AppCompatActivity {

    private EditText textBoxReview;
    private WriteReviewPresenter writeReviewPresenter;
    private String title, url;
    private ImageView poster;
    private TextView titleMovie;
    private FloatingActionButton backButton;
    private int idMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        writeReviewPresenter = new WriteReviewPresenter(this);

        titleMovie = findViewById(R.id.movieTitle);
        poster = findViewById(R.id.moviePoster);
        textBoxReview = findViewById(R.id.editTextBoxReview);
        backButton = findViewById(R.id.backButton);

        idMovie = getIntent().getIntExtra("MovieID", 0);
        title = getIntent().getStringExtra("MovieTitle");
        url = getIntent().getStringExtra("MovieUrl");

        titleMovie.setText(title);
        Glide.with(WriteReviewActivity.this.getApplicationContext())
                .load("http://image.tmdb.org/t/p/original"+ url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);

        backButton.setOnClickListener(view -> onBackPressed());

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

        buttonConfirmReview.setOnClickListener(view ->
                new MaterialAlertDialogBuilder(getActivityContext(), R.style.ThemeMyAppDialogAlertDay)
                .setTitle("Confirm review")
                .setMessage(R.string.confirm_review)
                .setPositiveButton("Yes", (dialogInterface, i) ->
                        writeReviewPresenter.clickAddReview(idMovie, textBoxReview.getText().toString().trim()))
                .setNegativeButton("Back", (dialogInterface, i) -> dialogInterface.dismiss())
                .show());
    }

    public Context getActivityContext(){
        return this;
    }

    public void closeActivity(){
        setResult(Activity.RESULT_OK, new Intent().putExtra("written", true));
        finish();
    }
}