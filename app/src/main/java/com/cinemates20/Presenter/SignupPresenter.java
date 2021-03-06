package com.cinemates20.Presenter;

import android.util.Log;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Model.User;
import com.cinemates20.R;

import com.cinemates20.View.SignupTabFragment;
import com.cinemates20.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class SignupPresenter {

    private final SignupTabFragment signupTabFragment;

    public SignupPresenter(SignupTabFragment signupTabFragment) {
        this.signupTabFragment = signupTabFragment;
    }

    public void onClickSignUp(String username, String mail, String psw, String confirmPsw)  {
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);

        signupTabFragment.setErrorToNull();

        if(isValidPassword(psw) && psw.equals(confirmPsw) &&
                isValidEmail(mail) && !userDAO.checkIfUsernameExists(username) && isValidUsername(username) ){
            createUser(mail, psw, username);
        }else{
            if(!isValidPassword(psw))
                signupTabFragment.setErrorInvalidPassword();

            if(!psw.equals(confirmPsw))
                signupTabFragment.setErrorNotMatchingPassword();

            if(!isValidEmail(mail))
                signupTabFragment.setErrorInvalidMail();

            if(!isValidUsername(username))
                signupTabFragment.setErrorUsername("Invalid username");

            if(userDAO.checkIfUsernameExists(username))
                signupTabFragment.setErrorUsername("Username already taken");
        }

    }

    private void createUser(String email, String password, String username){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(signupTabFragment.requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("success", "createUserWithEmail:success");
                        sendEmailVerification();

                        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);
                        userDAO.saveUser(username, email);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d("SignUpPresenter", "User profile updated.");
                                    }
                                });

                        User.setCurrentUser(username);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("failure", "createUserWithEmail:failure", task.getException());
                        Utils.showErrorDialog(signupTabFragment.getContext(), "Error", signupTabFragment.getString(R.string.signup_failed));
                    }
                });
    }

    private void sendEmailVerification() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Utils.showDialog(signupTabFragment.requireContext(),
                                "Account created!",
                                signupTabFragment.getString(R.string.mail_inviata));
                    }
                });
    }

    private static boolean isValidEmail(CharSequence target) {
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

    public static boolean isValidUsername(CharSequence target){
        final Pattern USERNAME_PATTERN =
                Pattern.compile("^" +
                        "(?=\\S+$)" +
                        ".{1,20}" +
                        "$");
        return USERNAME_PATTERN.matcher(target).matches();
    }
}
