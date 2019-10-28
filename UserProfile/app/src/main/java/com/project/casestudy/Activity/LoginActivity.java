package com.project.casestudy.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.casestudy.Constants;
import com.project.casestudy.Presenter.LoginPresenter;
import com.project.casestudy.R;
import com.project.casestudy.Util;
import com.project.casestudy.Validation;
import com.project.casestudy.View.LoginView;

public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private EditText userEmail;
    private EditText userPassword;
    private Button login;
    private TextView register;
    private CheckBox rememberMe;
    private String email;
    private String password;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private LoginPresenter loginPresenter;
    private Validation validation;
    private SharedPreferences sharedPreferencesUserId;
    private SharedPreferences.Editor editorUserId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        userEmail = (EditText) findViewById(R.id.input_email);
        userPassword = (EditText) findViewById(R.id.input_password);
        rememberMe = (CheckBox) findViewById(R.id.remember_me);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");
        userEmail.addTextChangedListener(new LoginTextWatcher(userEmail));
        userPassword.addTextChangedListener(new LoginTextWatcher(userPassword));
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        validation = new Validation(this);
        progressDialog = new ProgressDialog(this);
        loginPresenter = new LoginPresenter(this, database, reference,progressDialog);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.USER_ID, 0);
        String userId = sharedPreferences.getString(Constants.USER_ID, "");
        if (!Util.isNullOrEmpty(userId)) {
            Intent intent = ProfileActivity.newIntent(this, userId, true);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (Util.isNetworkConnected(this)) {
                    email = userEmail.getText().toString().trim();
                    password = userPassword.getText().toString().trim();
                    submit(email, password);
                }else{
                    toast(this,getResources().getString(R.string.error_internet_connected));
                }
                break;
            case R.id.register:
                Intent intent = SignUpActivity.newIntent(this);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void submit(String email, String password) {
        if (!validation.validateEmail(email, inputLayoutEmail, userEmail)) {
            return;
        }
        if (!validation.validatePassword(password, inputLayoutPassword, userPassword)) {
            return;
        }
        Util.hideKeyboard(this);
        loginPresenter.performLogin(this, email, password);
    }

    @Override
    public void loginSuccess(String userId) {
        if (rememberMe.isChecked()) {
            sharedPreferencesUserId = this.getSharedPreferences(Constants.USER_ID, 0);
            editorUserId = sharedPreferencesUserId.edit();
            editorUserId.putString(Constants.USER_ID, userId);
            editorUserId.commit();
        }
        Intent intent = ProfileActivity.newIntent(this, userId, true);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginError() {
        toast(this,getResources().getString(R.string.error_login));
    }

    public class LoginTextWatcher implements TextWatcher {
        private View view;

        public LoginTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.input_email:
                    validation.validateEmail(userEmail.getText().toString().trim(), inputLayoutEmail, userEmail);
                    break;
                case R.id.input_password:
                    validation.validatePassword(userPassword.getText().toString().trim(), inputLayoutPassword, userPassword);
                    break;
            }
        }
    }
}
