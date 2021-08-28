package com.cinemates20.Presenter;

import android.util.Log;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.R;

import com.cinemates20.View.SignupTabFragment;
import com.cinemates20.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignupPresenter {

    private final SignupTabFragment signupTabFragment;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public SignupPresenter(SignupTabFragment signupTabFragment) {
        this.signupTabFragment = signupTabFragment;
    }

    public void onClickSignUp()  {
        String username = signupTabFragment.getUsername().toLowerCase();
        String mail = signupTabFragment.getEmail();
        String psw = signupTabFragment.getPassword();
        String confirmPsw = signupTabFragment.getConfirmPsw();

        UserDAO userDAO = new UserDAO_Firestore(signupTabFragment.requireContext());

        signupTabFragment.setErrorToNull();

        if(isValidPassword(psw) && psw.equals(confirmPsw) &&
                isValidEmail(mail) && !userDAO.checkIfUsernameExists(username)){
            createUser(mail, psw, username);
        }else{
            if(!isValidPassword(psw))
                signupTabFragment.setErrorInvalidPassword();

            if(!psw.equals(confirmPsw))
                signupTabFragment.setErrorNotMatchingPassword();

            if(!isValidEmail(mail))
                signupTabFragment.setErrorInvalidMail();

            if(userDAO.checkIfUsernameExists(username))
                signupTabFragment.setErrorUsername();
        }

    }

    public void createUser(String email, String password, String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(signupTabFragment.requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("success", "createUserWithEmail:success");
                        //FirebaseUser user = mAuth.getCurrentUser();
                        sendEmailVerification();
                        UserDAO userDAO = new UserDAO_Firestore(signupTabFragment.requireContext());
                        userDAO.saveUser(username, email);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("failure", "createUserWithEmail:failure", task.getException());
                        Utils.showErrorDialog(signupTabFragment.getContext(), "Errore", signupTabFragment.getString(R.string.signup_failed));
                    }
                });
    }

    public void sendEmailVerification() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Utils.showDialog(signupTabFragment.requireContext(),
                                "Account creato",
                                signupTabFragment.getString(R.string.mail_inviata));
                    }
                });
    }

    public static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPassword(CharSequence target) {
        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[!@#$%^&+=])" +     // at least 1 special character
                        "(?=\\S+$)" +            // no white spaces
                        "(?=.*[A-Z])" +
                        ".{8,}" +                // at least 8 characters
                        "$");
        return PASSWORD_PATTERN.matcher(target).matches();
    }
}
