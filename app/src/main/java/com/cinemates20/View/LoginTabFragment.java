package com.cinemates20.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cinemates20.FacebookActivity;
import com.cinemates20.GoogleActivity;
import com.cinemates20.Presenter.LoginPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;

import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseAuth;

public class LoginTabFragment extends Fragment {

    private TextInputLayout textInputLayoutUsername, textInputLayoutPassword;
    private EditText editTextUsername, editTextPassword;
    private TextView textForgetPass, labelTextView;
    private Button loginRegular, signInButtonGoogle, signInButtonFacebook;
    private LoginPresenter loginPresenter;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        signInButtonGoogle = root.findViewById(R.id.buttonLoginWithGoogle);
        textInputLayoutUsername = root.findViewById(R.id.usernameLogin);
        editTextUsername = root.findViewById(R.id.textUsernameLogin);
        textInputLayoutPassword = root.findViewById(R.id.passwordLogin);
        editTextPassword = root.findViewById(R.id.textPasswordLogin);
        textForgetPass = root.findViewById(R.id.textForgetPass);
        loginRegular = root.findViewById(R.id.buttonLoginRegular);
        signInButtonFacebook = root.findViewById(R.id.buttonLoginWithFacebook);
        labelTextView = root.findViewById(R.id.label);
        loginPresenter = new LoginPresenter(this);

        animationLogin();

        eventListener();

        editTextUsername.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);

        return root;
    }

    public void eventListener() {
        signInButtonFacebook.setOnClickListener(view ->
                startActivity(new Intent(getContext(), FacebookActivity.class)));

        signInButtonGoogle.setOnClickListener(view -> {
            mAuth = FirebaseAuth.getInstance();
            startActivity(new Intent(getContext(), GoogleActivity.class));
        });

        textForgetPass.setOnClickListener(view ->
                startActivity(new Intent(getContext(), ResetPasswordActivity.class)));
    }

    /*
    If the fields are empty the use can't click the regular login button
     */
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String user = editTextUsername.getText().toString().trim();
            String psw = editTextPassword.getText().toString().trim();

            if(!Utils.checkIfFieldIsEmpty(user, psw)){
                loginRegular.setEnabled(true);
                onClickRegularLogin();
            }else{
                loginRegular.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    public void onClickRegularLogin(){
        loginRegular.setOnClickListener(view -> loginPresenter.checkIfTheAccountExists
                (editTextUsername.getText().toString().trim(), editTextPassword.getText().toString().trim()));
    }

    public void animationLogin() {
        textInputLayoutPassword.setTranslationX(800);
        textInputLayoutUsername.setTranslationX(800);
        textForgetPass.setTranslationX(800);
        loginRegular.setTranslationX(800);
        editTextUsername.setTranslationX(800);
        editTextPassword.setTranslationX(800);
        signInButtonGoogle.setTranslationY(300);
        signInButtonFacebook.setTranslationY(300);
        labelTextView.setTranslationY(300);

        textInputLayoutPassword.setAlpha(0);
        textInputLayoutUsername.setAlpha(0);
        textForgetPass.setAlpha(0);
        loginRegular.setAlpha(0);
        editTextUsername.setAlpha(0);
        editTextPassword.setAlpha(0);
        signInButtonGoogle.setAlpha(0);
        signInButtonFacebook.setAlpha(0);
        labelTextView.setAlpha(0);

        textInputLayoutUsername.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        textInputLayoutPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        editTextUsername.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        editTextPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        textForgetPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginRegular.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        signInButtonGoogle.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(800).start();
        signInButtonFacebook.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(800).start();
        labelTextView.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(800).start();
    }
}
