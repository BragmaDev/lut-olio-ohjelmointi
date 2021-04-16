package com.example.ht;

import java.util.HashMap;

public class UserManager {

    private User user = new User("Jarkko");
    private HashMap<String, User> users = null;
    private String current_password = "";

    private static UserManager um = new UserManager();
    private UserManager() {}
    public static UserManager getInstance() { return um; }

    public void createUser(String name, String password) {
        User new_user = new User(name);
        users.put(password, user);
    }

    public void logIn(String name, String password) {
        if (users.get(password).getName().equals(name)) {
            user = users.get(password);
            current_password = password;
        }
    }

    public void removeUser() {
        user = null;
        users.remove(current_password);
        current_password = "";
    }

    public User getUser() { return user; }

}
