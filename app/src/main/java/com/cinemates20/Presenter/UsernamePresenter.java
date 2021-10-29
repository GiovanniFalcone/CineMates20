package com.cinemates20.Presenter;

import android.content.Intent;
import android.util.Log;

import com.cinemates20.Model.DAO.DAOFactory;
import com.cinemates20.Model.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.NavigationActivity;
import com.cinemates20.View.UsernameActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsernamePresenter {

    private final UsernameActivity usernameActivity;

    public UsernamePresenter (UsernameActivity usernameActivity){
        this.usernameActivity = usernameActivity;
    }

    /**
     * After social sign in the user have to choose his username
     * If it doesn't exists, save user Data into Firestore db and show the home
     */
    public void onChooseUsername(){
        String username = usernameActivity.getUsername().toLowerCase();

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.FIREBASE);
        UserDAO userDAO = daoFactory.getUserDAO();

        boolean isExists = userDAO.checkIfUsernameExists(username);
        usernameActivity.setErrorToNull();

        if(isExists)
            usernameActivity.setErrorUsername();
        else{
            Utils.showDialog(usernameActivity.getActivityContext(), "Access completed!", "You have completed the login, now you will be able to access the home! ");
            userDAO.saveUser(username, usernameActivity.getCurrentUser());

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(usernameActivity.getUsername())
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Log.d("UsernamePresenter", "User profile updated.");
                        }
                    });

            usernameActivity.startActivity(new Intent(usernameActivity.getActivityContext(), NavigationActivity.class));
        }
    }

}
