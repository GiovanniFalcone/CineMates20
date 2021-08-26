package com.cinemates20.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cinemates20.Model.Comment;
import com.cinemates20.Presenter.WriteCommentPresenter;
import com.cinemates20.Utils.Adapters.CommentUserAdapter;
import com.cinemates20.Presenter.ReviewCardPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.cinemates20.R.*;

public class ReviewCardActivity extends AppCompatActivity {

    private int flag = 0;
    private ImageView buttonBack, buttonReportMenu;
    private FloatingActionButton buttonLike, buttonDislike, buttonLove, buttonClapping, buttonGrrr;
    private TextView nameAuthorView, reviewView, numberReactionView;
    private EditText writeComment;
    private RecyclerView recyclerView;
    private RatingBar ratingBar;
    private ReviewCardPresenter reviewCardPresenter;
    private WriteCommentPresenter writeCommentPresenter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_review_card);

        reviewCardPresenter = new ReviewCardPresenter(this);
        writeCommentPresenter = new WriteCommentPresenter(this);

        buttonBack = findViewById(id.imageViewBackButton);
        buttonReportMenu = findViewById(id.imageViewMenuRecensione);
        buttonLike = findViewById(id.likeButton);
        buttonDislike = findViewById(id.dislikeButton);
        buttonLove = findViewById(id.loveButton);
        buttonClapping = findViewById(id.clappingButton);
        buttonGrrr = findViewById(id.grrButton);
        nameAuthorView = findViewById(id.authorEditText);
        reviewView = findViewById(id.textReviewAuthor);
        numberReactionView = findViewById(id.seeAllReaction);
        writeComment = findViewById(id.editTextScriviCommento);
        recyclerView = findViewById(id.recyclerView);
        ratingBar = findViewById(id.valutationReview);

        nameAuthorView.setText("Recensione di " + getAuthor());

        reviewView.setMovementMethod(new ScrollingMovementMethod());

        reviewCardPresenter.viewReview();
        reviewCardPresenter.setUserCommentByReview();
        onClickEvents();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reviewCardPresenter.viewReview();
        reviewCardPresenter.setUserCommentByReview();
    }

    public String getAuthor(){
        return getIntent().getStringExtra("Author");
    }

    public String getTitleMovie(){
        return getIntent().getStringExtra("titleMovie");
    }

    public String getIdReview(){return getIntent().getStringExtra("idReview");}

    public void setReview(String review){
        reviewView.setText(review);
    }

    public void setRecycler(List<Comment> authorList) {
        Log.d("Passo", "Sono in setRecycler");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CommentUserAdapter commentUserAdapter = new CommentUserAdapter(this, authorList);
        Log.d("Passo", "Sono in setRecycler prima di setAdapter");
        recyclerView.setAdapter(commentUserAdapter);
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

        buttonBack.setOnClickListener(view -> onBackPressed());

        numberReactionView.setOnClickListener(view ->
                reviewCardPresenter.onClickNumberReactions());

        writeComment.setOnEditorActionListener((textView, i, keyEvent) -> {
            writeCommentPresenter.clickAddComment(String.valueOf(writeComment.getText()));
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(writeComment.getWindowToken(), 0);
            writeComment.setFocusable(false);
            writeComment.setFocusableInTouchMode(true);
            return true;
        });
    }

    public void clickButtonReaction(String buttonClicked, FloatingActionButton buttonSelected) {
        boolean state = buttonSelected.isSelected();

        if(!state)
            buttonSelected.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(color.gold, getTheme())));
        else
            buttonSelected.setBackgroundTintList(ColorStateList.valueOf(this.getResources().getColor(color.white, getTheme())));

        reviewCardPresenter.manageReactionClicked(state, buttonClicked);
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
}