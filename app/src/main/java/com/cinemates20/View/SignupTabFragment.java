package com.cinemates20.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.cinemates20.Presenter.SignupPresenter;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class SignupTabFragment extends Fragment {

    private TextInputLayout textInputLayoutUsername, textInputLayoutEmail, textInputLayoutPassword, textInputLayoutConfermaPassword;
    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button signupButton;
    private SignupPresenter signupPresenter;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        signupPresenter = new SignupPresenter(this);

        textInputLayoutUsername = root.findViewById(R.id.usernameSignup);
        editTextUsername = root.findViewById(R.id.textUsernameSignup);
        textInputLayoutPassword = root.findViewById(R.id.passwordSignup);
        editTextPassword = root.findViewById(R.id.textPasswordSignup);
        textInputLayoutEmail = root.findViewById(R.id.emailSignup);
        editTextEmail = root.findViewById(R.id.textEmailSignup);
        textInputLayoutConfermaPassword = root.findViewById(R.id.confermaPasswordSignup);
        editTextConfirmPassword = root.findViewById(R.id.textConfermaPasswordSignup);
        signupButton = root.findViewById(R.id.buttonSignup);

        editTextUsername.addTextChangedListener(textWatcher);
        editTextEmail.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);
        editTextConfirmPassword.addTextChangedListener(textWatcher);

        return root;
    }

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String username = editTextUsername.getText().toString().trim().toLowerCase();
            String mail = editTextEmail.getText().toString().trim();
            String psw = editTextPassword.getText().toString().trim();
            String confirmPsw = editTextConfirmPassword.getText().toString().trim();

            if(!Utils.checkIfFieldIsEmpty(username, mail, psw, confirmPsw)){
                signupButton.setEnabled(true);
                signupButton.setOnClickListener(view -> { signupPresenter.onClickSignUp(username, mail, psw, confirmPsw);
                });
            }else{
                signupButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void setErrorInvalidPassword(){
        textInputLayoutPassword.setError("Invalid password");
    }

    public void setErrorToNull(){
        textInputLayoutUsername.setError(null);
        textInputLayoutEmail.setError(null);
        textInputLayoutPassword.setError(null);
        textInputLayoutConfermaPassword.setError(null);
    }

    public void setErrorNotMatchingPassword(){
        textInputLayoutConfermaPassword.setError("The password doesn't match");
    }

    public void setErrorInvalidMail(){
        textInputLayoutEmail.setError("Invalid email");
    }

    public void setErrorUsername(String error) {
        textInputLayoutUsername.setError(error);
    }
}
