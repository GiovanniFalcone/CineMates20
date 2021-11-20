package com.cinemates20.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cinemates20.Model.Comment;
import com.cinemates20.Model.Review;
import com.cinemates20.Presenter.ReportPresenter;
import com.cinemates20.Presenter.WriteCommentPresenter;
import com.cinemates20.R;
import com.cinemates20.Presenter.ReviewCardPresenter;
import com.cinemates20.Utils.Adapters.GenericAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.cinemates20.R.*;

public class ReviewCardActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private int flag = 0;       //for reactions
    private ImageView authorIcon, userIcon;
    private FloatingActionButton buttonLike, buttonDislike, buttonLove, buttonClapping, buttonGrrr, buttonSend;
    private TextView nameAuthorView, reviewView;
    private Button numberReactionView;
    private EditText writeComment;
    private RecyclerView recyclerView;
    private RatingBar ratingBar;
    private Toolbar toolbar;
    private View layout_noResults;
    private ReviewCardPresenter reviewCardPresenter;
    private WriteCommentPresenter writeCommentPresenter;
    private ReportPresenter reportPresenter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_review_card);

        reviewCardPresenter = new ReviewCardPresenter(this);
        writeCommentPresenter = new WriteCommentPresenter(this);
        reportPresenter = new ReportPresenter(this);

        toolbar = findViewById(R.id.toolbar);
        buttonLike = findViewById(id.likeButton);
        buttonDislike = findViewById(id.dislikeButton);
        buttonLove = findViewById(id.loveButton);
        buttonClapping = findViewById(id.clappingButton);
        buttonGrrr = findViewById(id.grrButton);
        buttonSend = findViewById(id.sendComment);
        nameAuthorView = findViewById(id.authorName);
        reviewView = findViewById(id.textReviewAuthor);
        numberReactionView = findViewById(id.seeAllReaction);
        writeComment = findViewById(id.editTextScriviCommento);
        recyclerView = findViewById(id.recyclerView);
        ratingBar = findViewById(id.valutationReview);
        authorIcon = findViewById(id.authorPropic);
        userIcon = findViewById(id.userPropic);
        layout_noResults = findViewById(id.layout_noResults);

        if(!getIntent().getBooleanExtra("PersonalReview", false)) {
            toolbar.inflateMenu(menu.toolbar_menu);
            toolbar.setOnMenuItemClickListener(this);
        }

        setReviewCard();

        reviewView.setMovementMethod(new ScrollingMovementMethod());

        onClickEvents();
    }

    private void setReviewCard() {
        nameAuthorView.setText(String.format("%s", getReview().getAuthor()));
        Glide.with(this)
                .load(getIntent().getStringExtra("Icon"))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        startPostponedEnterTransition();
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleCrop())
                .into(authorIcon);
        reviewView.setText(getReview().getTextReview());
        ratingBar.setRating(getReview().getRating());

        reviewCardPresenter.viewReview();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reviewCardPresenter.viewReview();
    }

    public Review getReview(){
        return (Review) getIntent().getParcelableExtra("Review");
    }

    public void setReview(String review){
        reviewView.setText(review);
    }

    public void setUserIcon(String url){
        if(!url.equals("")) {
            Glide.with(getApplicationContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CircleCrop())
                    .into(userIcon);
        }
    }

    public void setRecycler(List<Comment> authorList) {
        if(!authorList.isEmpty()) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            GenericAdapter<Comment> commentUserAdapter = new GenericAdapter<>(authorList, this);
            recyclerView.setAdapter(commentUserAdapter);
            clickOption(commentUserAdapter);
        }
    }

    public void setView(boolean value){
        if(value)
            layout_noResults.setVisibility(View.VISIBLE);
        else
            layout_noResults.setVisibility(View.GONE);
    }

    public void clickOption(GenericAdapter<Comment> commentUserAdapter){
        commentUserAdapter.setOnItemClickListener(new GenericAdapter.ClickListener() {
            @Override
            public void onItemClickListener(Comment commentSelected) {
                reportPresenter.onClickOption(commentSelected);
            }
        });
    }

    public void onClickEvents(){
        buttonLike.setOnClickListener(view -> {
            clickButtonReaction("like", buttonLike);
            buttonLike.setSelected(!buttonLike.isSelected());

            if(flag != 1) changeButtonState(flag);

            if(buttonLike.isSelected())
                flag = 1;
            else
                flag = 0;
        });

        buttonDislike.setOnClickListener(view -> {
            clickButtonReaction("dislike", buttonDislike);
            buttonDislike.setSelected(!buttonDislike.isSelected());

            if(flag != 2) changeButtonState(flag);

            if(buttonDislike.isSelected())
                flag = 2;
            else
                flag = 0;
        });

        buttonLove.setOnClickListener(view -> {
            clickButtonReaction("love", buttonLove);
            buttonLove.setSelected(!buttonLove.isSelected());

            if(flag != 3) changeButtonState(flag);

            if(buttonLove.isSelected())
                flag = 3;
            else
                flag = 0;
        });

        buttonClapping.setOnClickListener(view -> {
            clickButtonReaction("clap", buttonClapping);
            buttonClapping.setSelected(!buttonClapping.isSelected());

            if(flag != 4) changeButtonState(flag);

            if(buttonClapping.isSelected())
                flag = 4;
            else
                flag = 0;
        });

        buttonGrrr.setOnClickListener(view -> {
            clickButtonReaction("grrr", buttonGrrr);
            buttonGrrr.setSelected(!buttonGrrr.isSelected());

            if(flag != 5) changeButtonState(flag);

            if(buttonGrrr.isSelected())
                flag = 5;
            else
                flag = 0;
        });

        toolbar.setNavigationOnClickListener(view13 -> onBackPressed());

        numberReactionView.setOnClickListener(view ->
                reviewCardPresenter.onClickNumberReactions(getReview().getIdReview()));

        buttonSend.setEnabled(false);
        writeComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonSend.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(writeComment.getText().toString().isEmpty())
                    buttonSend.setEnabled(false);

            }
        });

        buttonSend.setOnClickListener(view -> {
            writeCommentPresenter.clickAddComment(String.valueOf(writeComment.getText()), getReview().getIdReview());
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(writeComment.getWindowToken(), 0);
            writeComment.setFocusable(false);
            writeComment.setFocusableInTouchMode(true);
            writeComment.getText().clear();
        });
    }

    public void clickButtonReaction(String buttonClicked, FloatingActionButton buttonSelected) {
        boolean state = buttonSelected.isSelected();

        if(!state)
            buttonSelected.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(color.gold, getTheme())));
        else
            buttonSelected.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(color.white, getTheme())));

        reviewCardPresenter.manageReactionClicked(getReview().getIdReview(), state, buttonClicked);
    }

    public void setColorButton(String buttonType){
        switch (buttonType) {
            case "like":
                buttonLike.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(color.gold, getTheme())));
                buttonLike.setSelected(!buttonLike.isSelected());
                break;
            case "dislike":
                buttonDislike.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(color.gold, getTheme())));
                buttonDislike.setSelected(!buttonDislike.isSelected());
                break;
            case "love":
                buttonLove.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(color.gold, getTheme())));
                buttonLove.setSelected(!buttonLove.isSelected());
                break;
            case "clap":
                buttonClapping.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(color.gold, getTheme())));
                buttonClapping.setSelected(!buttonClapping.isSelected());
                break;
            case "grrr":
                buttonGrrr.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(color.gold, getTheme())));
                buttonGrrr.setSelected(!buttonGrrr.isSelected());
                break;
        }
    }

    public void changeButtonState(int flag){
        switch (flag) {
            case 1:
                buttonLike.setSelected(buttonLike.isSelected());
                clickButtonReaction("like", buttonLike);
                buttonLike.setSelected(!buttonLike.isSelected());
                break;
            case 2:
                buttonDislike.setSelected(buttonDislike.isSelected());
                clickButtonReaction("dislike", buttonDislike);
                buttonDislike.setSelected(!buttonDislike.isSelected());
                break;
            case 3:
                buttonLove.setSelected(buttonLove.isSelected());
                clickButtonReaction("love", buttonLove);
                buttonLove.setSelected(!buttonLove.isSelected());
                break;
            case 4:
                buttonClapping.setSelected(buttonClapping.isSelected());
                clickButtonReaction("clap", buttonClapping);
                buttonClapping.setSelected(!buttonClapping.isSelected());
                break;
            case 5:
                buttonGrrr.setSelected(buttonGrrr.isSelected());
                clickButtonReaction("grrr", buttonGrrr);
                buttonGrrr.setSelected(!buttonGrrr.isSelected());
                break;
        }
    }

    public void setFlag(String buttonType){
        switch (buttonType) {
            case "like":
                flag = 1;
                break;
            case "dislike":
                flag = 2;
                break;
            case "love":
                flag = 3;
                break;
            case "clap":
                flag = 4;
                break;
            case "grrr":
                flag = 5;
                break;
        }
    }

    public Context getActivityContext(){
        return this;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case id.reportForSpoiler:
                reportPresenter.reportClicked(getReview().getIdReview(), "spoiler");
                break;

            case R.id.reportForLanguage:
                reportPresenter.reportClicked(getReview().getIdReview(), "language");
                break;

            case id.rateReview:
                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeMyAppDialogAlertDay);
                builder.setTitle("Rate this review");
                View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_rate, (ViewGroup) findViewById(android.R.id.content),false);
                RatingBar ratingBar = viewDialog.findViewById(R.id.ratingBar);
                builder.setView(viewDialog);
                builder.setMessage(R.string.confirm_valuation);
                builder.setPositiveButton("Rate", (dialogInterface, i) -> {
                    reviewCardPresenter.rateReview(ratingBar.getRating(), getReview().getIdReview());
                })
                        .setNegativeButton("Back", (dialogInterface, i) -> dialogInterface.dismiss());

                AlertDialog alertDialog = builder.create();
                ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) ->
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(ratingBar1.getRating() != 0.0));
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                break;
        }
        return false;
    }

    public void setRating(float rating) {
        ratingBar.setRating(rating);
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
}