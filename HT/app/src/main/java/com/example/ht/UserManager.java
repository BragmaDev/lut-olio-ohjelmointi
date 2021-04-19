package com.example.ht;

import java.util.ArrayList;

public class UserManager {

    private User user;
    private ArrayList<User> users = new ArrayList<>();

    private static UserManager um = new UserManager();
    private UserManager() {}
    public static UserManager getInstance() { return um; }

    public void createUser(String name, String password) {
        User new_user = new User(name, password);
        users.add(new_user);
    }

    public boolean switchUser(String name, String password) {
        System.out.println(name + " and the pass is " + password);
        if (users.size() > 0) {
            for (User u : users) {
                if (u.getName().equals(name) && u.getPassword().equals(password)) {
                    user = u;
                    return true;
                }
            }
        }
        return false;
    }

    public void removeUser() {
        User previous_user = user;
        user = null;
        users.remove(previous_user);
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public ArrayList<User> getUsers() { return users; }

}
