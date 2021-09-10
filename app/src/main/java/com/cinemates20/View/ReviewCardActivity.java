package com.cinemates20.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cinemates20.DAO.Implements.CommentDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.CommentDAO;
import com.cinemates20.Model.Comment;
import com.cinemates20.Presenter.ReportPresenter;
import com.cinemates20.Presenter.WriteCommentPresenter;
import com.cinemates20.Utils.Adapters.CommentUserAdapter;
import com.cinemates20.Presenter.ReviewCardPresenter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cinemates20.R.*;

public class ReviewCardActivity extends AppCompatActivity {

    private int flag = 0;
    private ImageView buttonBack, buttonReportMenu;
    private FloatingActionButton buttonLike, buttonDislike, buttonLove, buttonClapping, buttonGrrr, buttonSend;
    private TextView nameAuthorView, reviewView;
    private Button numberReactionView;
    private EditText writeComment;
    private RecyclerView recyclerView;
    private CommentUserAdapter commentUserAdapter;
    private RatingBar ratingBar;
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

        buttonBack = findViewById(id.imageViewBackButton);
        buttonReportMenu = findViewById(id.imageViewMenuRecensione);
        buttonLike = findViewById(id.likeButton);
        buttonDislike = findViewById(id.dislikeButton);
        buttonLove = findViewById(id.loveButton);
        buttonClapping = findViewById(id.clappingButton);
        buttonGrrr = findViewById(id.grrButton);
        buttonSend = findViewById(id.sendComment);
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        commentUserAdapter = new CommentUserAdapter(this, authorList);
        recyclerView.setAdapter(commentUserAdapter);
        clickOption(commentUserAdapter);
    }

    public void clickOption(CommentUserAdapter commentUserAdapter){
        commentUserAdapter.setOnItemClickListener(new CommentUserAdapter.ClickListener() {
            @Override
            public void onItemClickListener() {
                reportPresenter.onClickOption();
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

        buttonBack.setOnClickListener(view -> onBackPressed());

        numberReactionView.setOnClickListener(view ->
                reviewCardPresenter.onClickNumberReactions());

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
            writeCommentPresenter.clickAddComment(String.valueOf(writeComment.getText()));
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

    public Context getActivityContext(){
        return this;
    }
}