package com.cinemates20.Presenter;

import android.content.Intent;

import com.cinemates20.DAO.Implements.UserDAO_Firestore;
import com.cinemates20.DAO.Interface.Firestore.UserDAO;
import com.cinemates20.Utils.Utils;
import com.cinemates20.View.NavigationActivity;
import com.cinemates20.View.UsernameActivity;

public class UsernamePresenter {

    private UsernameActivity usernameActivity;
    private UserDAO userDAO;

    public UsernamePresenter (UsernameActivity usernameActivity){
        this.usernameActivity = usernameActivity;
    }

    /**
     * After social sign in the user have to choose his username
     * If it doesn't exists, save user Data into Firestore db and show the home
     */
    public void onChooseUsername(){
        String username = usernameActivity.getUsername().toLowerCase();
        userDAO = new UserDAO_Firestore(usernameActivity.getApplicationContext());
        boolean isExists = userDAO.checkIfUsernameExists(username);
        usernameActivity.setErrorToNull();
        if(isExists)
            usernameActivity.setErrorUsername();
        else{
            Utils.showDialog(usernameActivity.getActivityContext(), "Accesso completato!", "Hai completato l'accesso, ora potrai accedere alla home!");
            userDAO.saveUser(username, usernameActivity.getCurrentUser());
            usernameActivity.startActivity(new Intent(usernameActivity.getActivityContext(), NavigationActivity.class));
        }
    }

}
