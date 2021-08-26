package com.cinemates20.Model;

import java.util.List;

public class User {

    private String username, email, icon;
    private List<String> friends, movieLists;

    public User(){ }

    public User(String username, String email, String icon, List<String> friends, List<String> movieLists){
        this.username = username;
        this.email = email;
        this.icon = icon;
        this.friends = friends;
        this.movieLists = movieLists;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        email = mail;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getMovieLists() {
        return movieLists;
    }

    public void setMovieLists(List<String> movieLists) {
        this.movieLists = movieLists;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", icon='" + icon + '\'' +
                ", friends=" + friends + '\'' +
                ", movieLists=" + movieLists +
                '}';
    }
}