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

    Context context;
    private User user;
    private ArrayList<User> users = new ArrayList<>();
    final String users_filename = "users.txt";

    private static UserManager um = new UserManager();
    private UserManager() {}
    public static UserManager getInstance() { return um; }

    public void setContext(Context context) { this.context = context; }

    public void createUser(String name, String password) {
        User new_user = new User(name, password);
        users.add(new_user);
        saveUsers();
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
        saveUsers();
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public ArrayList<User> getUsers() { return users; }

    public void saveUsers() {
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

    public void loadUsers() {
        try {
            System.out.println("LOADING...");
            FileInputStream fis = context.openFileInput(users_filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            users = (ArrayList<User>) is.readObject();
            is.close();
            fis.close();
            System.out.println("LOADED!");
            System.out.println(users.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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
