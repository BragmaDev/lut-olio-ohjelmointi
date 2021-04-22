package com.example.ht;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{12,30}$";

    public boolean validate(String password) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
