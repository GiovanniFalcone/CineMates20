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
     * @param email the email of current user
     * @param password the password of current user
     */
    public void checkIfTheAccountExists(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                    Intent intent = new Intent(loginTabFragment.getActivity(), NavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    loginTabFragment.startActivity(intent);
                    loginTabFragment.requireActivity().finish();
                }else{
                    Utils.showErrorDialog(loginTabFragment.getContext(), "The account is not verified", loginTabFragment.getString(R.string.non_verificato));
                }
            } else {
                Utils.showErrorDialog(loginTabFragment.getContext(), "Wrong credential", loginTabFragment.getString(R.string.credenziali_errate));
            }
        });
    }

}
