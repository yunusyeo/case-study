package com.project.casestudy;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class Validation {
    private Activity activity;

    public Validation(Activity activity) {
        this.activity = activity;
    }

    public boolean validateEmail(String email, TextInputLayout textInputLayout, EditText userEmail) {
        if (!Util.isValidEmail(email)) {
            textInputLayout.setError(activity.getString(R.string.validate_email));
            requestFocus(userEmail);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validatePassword(String password, TextInputLayout textInputLayout, EditText userPassword) {
        if (Util.isNullOrEmpty(password)) {
            textInputLayout.setError(activity.getString(R.string.validate_password));
            requestFocus(userPassword);
            return false;
        } else if (Util.isValidPassword(password)){
            textInputLayout.setError(activity.getString(R.string.validate_password_character));
            requestFocus(userPassword);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateName(String name, TextInputLayout textInputLayout, EditText userName) {
        if (Util.isNullOrEmpty(name)) {
            textInputLayout.setError(activity.getString(R.string.validate_name));
            requestFocus(userName);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateSurname(String password, TextInputLayout textInputLayout, EditText userSurname) {
        if (Util.isNullOrEmpty(password)) {
            textInputLayout.setError(activity.getString(R.string.validate_surname));
            requestFocus(userSurname);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
