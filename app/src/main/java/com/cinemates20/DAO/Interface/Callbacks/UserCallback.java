package com.cinemates20.DAO.Interface.Callbacks;

import com.cinemates20.Model.User;

import java.util.List;

public interface UserCallback {
    default void setList(List<User> listUser) {}
    default void isExists(boolean b) {}
    default void setUsername(String username) {}

}
