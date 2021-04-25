package com.example.ht;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class UserManager {

    private User user;
    private ArrayList<User> users = new ArrayList<>();
    final String users_filename = "users.txt";

    private static UserManager um = new UserManager();
    private UserManager() {}
    public static UserManager getInstance() { return um; }

    /* This method takes in a name, a password and the context and creates a new user, adding it to
    the arraylist */
    public void createUser(String name, String password, Context context) {
        User new_user = new User(name, password);
        users.add(new_user);
        saveUsers(context);
    }

    /* This method takes in a username and a password and if they match, it sets the current user to
    the corresponding one and returns true */
    public boolean switchUser(String name, String password) {
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

    // This method removes the current user from the arraylist of users
    public void removeUser(Context context) {
        User previous_user = user;
        user = null;
        users.remove(previous_user);
        saveUsers(context);
    }

    public User getUser() { return user; }
    public ArrayList<User> getUsers() { return users; }

    // This method saves the current 'users' object as a loadable file
    public void saveUsers(Context context) {
        if (context != null) {
            try {
                FileOutputStream fos = context.openFileOutput(users_filename, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(users);
                os.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // This method loads users and their info from the saved file
    public void loadUsers(Context context) {
        try {
            FileInputStream fis = context.openFileInput(users_filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            users = (ArrayList<User>) is.readObject();
            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // This method sets the user's current weight by finding the latest entry
    public void setUserWeight() {
        WeightEntry latest_entry = null;
        for (Entry e : user.getEntries(1)) {
            if (latest_entry == null || e.getDate().getTime() > latest_entry.getDate().getTime()) {
                latest_entry = (WeightEntry) e;
            }
        }
        user.setWeight(latest_entry.getWeight());
    }

}
