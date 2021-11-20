package com.cinemates20.Presenter;

import android.content.Intent;
import android.util.Log;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.InterfaceDAO.UserDAO;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.NavigationActivity;
import com.cinemates20.View.UsernameActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class UsernamePresenter {

    private final UsernameActivity usernameActivity;

    public UsernamePresenter (UsernameActivity usernameActivity){
        this.usernameActivity = usernameActivity;
    }

    /**
     * After social sign in the user have to choose his username
     * If it doesn't exists, save user Data into Firestore db and show the home
     */
    public void onChooseUsername(String username){
        UserDAO userDAO = DAOFactory.getUserDAO(DAOFactory.FIREBASE);

        boolean isExists = userDAO.checkIfUsernameExists(username);
        usernameActivity.setErrorToNull();

        if(isValidUsername(username)){
            if(isExists)
                usernameActivity.setErrorUsername("Username already taken.");
            else{
                Utils.showDialog(usernameActivity.getActivityContext(), "Access completed!", "You have completed the login, now you will be able to access the home! ");
                userDAO.saveUser(username, usernameActivity.getCurrentUser());

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.d("UsernamePresenter", "User profile updated.");
                            }
                        });

                usernameActivity.startActivity(new Intent(usernameActivity.getActivityContext(), NavigationActivity.class));
            }
        } else
            usernameActivity.setErrorUsername("Invalid username");
    }

    private static boolean isValidUsername(CharSequence target){
        final Pattern USERNAME_PATTERN =
                Pattern.compile("^" +
                        "(?=\\S+$)" +
                        ".{1,20}" +
                        "$");
        return USERNAME_PATTERN.matcher(target).matches();
    }
}
