package com.cinemates20.Presenter;

import android.content.Intent;

import com.cinemates20.View.LoginTabFragment;
import com.cinemates20.View.NavigationActivity;
import com.cinemates20.R;
import com.cinemates20.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginPresenter {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final LoginTabFragment loginTabFragment;

    public LoginPresenter(LoginTabFragment loginTabFragment) {
        this.loginTabFragment = loginTabFragment;
    }

    /**
     * If the account exists and email is verified shows home, error otherwise
     * @param email - the email of current user
     * @param password - the password of current user
     */
    public void checkIfTheAccountExists(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                    loginTabFragment.startActivity(new Intent(loginTabFragment.getActivity(), NavigationActivity.class));
                }else{
                    Utils.showErrorDialog(loginTabFragment.getContext(), "Account non verificato", loginTabFragment.getString(R.string.non_verificato));
                }
            } else {
                Utils.showErrorDialog(loginTabFragment.getContext(), "Credenziali errate", loginTabFragment.getString(R.string.credenziali_errate));
            }
        });
    }

    public boolean isLogged(){
        return mAuth.getCurrentUser()!=null && mAuth.getCurrentUser().isEmailVerified();
    }

}
