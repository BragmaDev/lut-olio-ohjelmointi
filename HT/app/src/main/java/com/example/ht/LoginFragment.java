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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    View view;
    MainActivity main;
    UserManager um = UserManager.getInstance();
    PasswordValidator validator = new PasswordValidator();
    ArrayList<String> usernames = new ArrayList<>();
    String selected_user = "";

    Spinner spinner_user;
    ArrayAdapter<String> adapter;
    EditText edit_login_password;
    EditText edit_new_user;
    EditText edit_new_password;
    TextView text_login_error;
    TextView text_create_error;
    Button button_login;
    Button button_create;

    public LoginFragment(MainActivity main) { this.main = main; }

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
        text_login_error = (TextView) view.findViewById(R.id.textLoginError);
        text_create_error = (TextView) view.findViewById(R.id.textCreateError);
        button_login = (Button) view.findViewById(R.id.buttonAddWeight);
        button_create = (Button) view.findViewById(R.id.buttonCreate);

        // Spinner setup
        adapter = new ArrayAdapter(getContext(), R.layout.spinner_layout, usernames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_user.setAdapter(adapter);
        spinner_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_user = parent.getItemAtPosition(position).toString();
                edit_login_password.setText("");
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

    /* This method attempts to log the user in using the inputted name and password and shows the
    error text in case the password is wrong */
    private void logIn(String username, String password) {
        boolean logged_in = um.switchUser(username, password);
        if (logged_in) {
            text_login_error.setText("");
            main.changeFragment("co2");
        } else {
            text_login_error.setText("Incorrect password");
        }
    }

    /* This method attempts to create a new user based on the inputted name and password and shows
    the error text in case the username is taken, empty or if the password doesn't meet the criteria */
    private void create() {
        if (!edit_new_user.getText().toString().isEmpty()) {
            if (!usernames.contains(edit_new_user.getText().toString())) {
                if (validator.validate(edit_new_password.getText().toString())) {
                    um.createUser(edit_new_user.getText().toString(), edit_new_password.getText().toString(), main.getApplicationContext());
                    edit_new_user.setText("");
                    edit_new_password.setText("");
                    text_create_error.setText("");
                } else {
                    text_create_error.setText("Invalid password");
                }
            } else {
                text_create_error.setText("A user with that name already exists");
            }
        } else {
            text_create_error.setText("Please enter a valid name");
        }
        updateUsernames();
    }

    // This method updates the username arraylist and notifies the spinner
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
