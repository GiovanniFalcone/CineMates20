package com.cinemates20.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.cinemates20.Presenter.UsernamePresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class UsernameActivity extends AppCompatActivity {

    private Button buttonSend;
    private TextInputLayout chooseUsernameLayout;
    private EditText editTextUsername;
    private UsernamePresenter usernamePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        usernamePresenter = new UsernamePresenter(this);
        chooseUsernameLayout = findViewById(R.id.chooseUsernameLayout);
        editTextUsername = findViewById(R.id.chooseUsername);
        buttonSend = findViewById(R.id.buttonSend);

        editTextUsername.addTextChangedListener(textWatcher);
    }

    public String getCurrentUser(){
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    }

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String username = editTextUsername.getText().toString().trim();

            if(!Utils.checkIfFieldIsEmpty(username)){
                buttonSend.setEnabled(true);
                buttonSend.setOnClickListener(v -> {
                    usernamePresenter.onChooseUsername(username.toLowerCase());
                });
            }else{
                buttonSend.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    public void setErrorUsername(String s) {
        chooseUsernameLayout.setError(s);
    }

    public void setErrorToNull(){
        chooseUsernameLayout.setError(null);
    }

    public Context getActivityContext(){
        return this;
    }
}