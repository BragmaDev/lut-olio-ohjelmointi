package com.example.ht;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    View view;
    MainActivity main = null;
    UserManager um = UserManager.getInstance();
    ArrayList<String> usernames = new ArrayList<>();
    String selected_user = "";

    Spinner spinner_user;
    ArrayAdapter<String> adapter;
    EditText edit_login_password;
    EditText edit_new_user;
    EditText edit_new_password;
    Button button_login;
    Button button_create;

    public LoginFragment(MainActivity main) { this.main = main; }
    public LoginFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinner_user = (Spinner) view.findViewById(R.id.spinnerUser);
        edit_login_password = (EditText) view.findViewById(R.id.editLoginPassword);
        edit_new_user = (EditText) view.findViewById(R.id.editNewUser);
        edit_new_password = (EditText) view.findViewById(R.id.editNewPassword);
        button_login = (Button) view.findViewById(R.id.buttonLogIn);
        button_create = (Button) view.findViewById(R.id.buttonCreate);

        // Spinner setup
        adapter = new ArrayAdapter(getContext(), R.layout.spinner_layout, usernames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_user.setAdapter(adapter);
        spinner_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_user = parent.getItemAtPosition(position).toString();
                System.out.println(selected_user);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { updateUsernames(); }
        });

        updateUsernames();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn(selected_user, edit_login_password.getText().toString());
            }
        });

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });
    }

    private void logIn(String username, String password) {
        boolean logged_in = um.switchUser(username, password);
        if (logged_in) {
            main.changeFragment("co2");
        }
    }

    private void create() {
        if (!usernames.contains(edit_new_user.getText().toString())) {
            um.createUser(edit_new_user.getText().toString(), edit_new_password.getText().toString());
            edit_new_user.setText("");
            edit_new_password.setText("");
        } else {
            System.out.println("User already exists!");
        }
        updateUsernames();
    }

    private void updateUsernames() {
        usernames.clear();
        if (um.getUsers().size() > 0) {
            for (User u : um.getUsers()) {
                usernames.add(u.getName());
            }
        }
        adapter.notifyDataSetChanged();
    }
}
