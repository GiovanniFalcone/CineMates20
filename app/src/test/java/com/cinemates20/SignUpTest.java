package com.cinemates20;

import com.cinemates20.Presenter.SignupPresenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SignUpTest {
    @Test
    public void isValidUsername_expectedTrue(){
        String username = "cinemates";
        Assertions.assertTrue(SignupPresenter.isValidUsername(username));
    }

    @Test
    public void isValidUsername_expectedFalse(){
        String username = "cine mates";
        //username with one or multiple space is not valid
        Assertions.assertFalse(SignupPresenter.isValidUsername(username));
    }

    @Test
    public void isValidUsername_expectedFalse_longUsername(){
        String username = "username.very.very.long";
        //username with 20 or more characters is not valid
        Assertions.assertFalse(SignupPresenter.isValidUsername(username));
    }

    @Test
    public void isValidPassword_expectedTrue(){
        String psw = "@29Ingsw";
        Assertions.assertTrue(SignupPresenter.isValidPassword(psw));
    }

    @Test
    public void isValidPassword_expectedFalse(){
        String psw = "ingsw";
        //password too weak
        Assertions.assertFalse(SignupPresenter.isValidPassword(psw));
    }
}
