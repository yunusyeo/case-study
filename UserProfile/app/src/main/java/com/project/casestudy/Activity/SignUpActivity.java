package com.project.casestudy.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.casestudy.Model.User;
import com.project.casestudy.Presenter.SignUpPresenter;
import com.project.casestudy.R;
import com.project.casestudy.Util;
import com.project.casestudy.Validation;
import com.project.casestudy.View.SignUpView;

public class SignUpActivity extends BaseActivity implements SignUpView, View.OnClickListener {
    private Toolbar toolbar;
    private TextInputLayout inputLayoutName, inputLayoutSurname, inputLayoutEmail, inputLayoutPassword;
    private EditText userName;
    private EditText userSurname;
    private EditText userEmail;
    private EditText userPassword;
    private Button signUp;
    private String name;
    private String surname;
    private String email;
    private String password;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private SignUpPresenter registerUpdatePresenter;
    private Validation validation;
    private ProgressDialog progressDialog;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.register_text));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutSurname = (TextInputLayout) findViewById(R.id.input_layout_surname);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        userName = (EditText) findViewById(R.id.input_name);
        userSurname = (EditText) findViewById(R.id.input_surname);
        userEmail = (EditText) findViewById(R.id.input_email);
        userPassword = (EditText) findViewById(R.id.input_password);
        signUp = (Button) findViewById(R.id.sign_up);

        userEmail.addTextChangedListener(new RegisterTextWatcher(userEmail));
        userPassword.addTextChangedListener(new RegisterTextWatcher(userPassword));

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");
        signUp.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        validation = new Validation(this);
        registerUpdatePresenter = new SignUpPresenter(this, database, reference, progressDialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up:
                if (Util.isNetworkConnected(this)) {
                    name = userName.getText().toString().trim();
                    surname = userSurname.getText().toString().trim();
                    email = userEmail.getText().toString().trim();
                    password = userPassword.getText().toString().trim();
                    submit(name, surname, email, password);
                } else {
                    toast(this, getResources().getString(R.string.error_internet_connected));
                }
                break;
        }
    }

    private void submit(String name, String surname, String email, String password) {
        //name alanı zorunlu olmayabilirdi.
        if (!validation.validateName(name, inputLayoutName, userName)) {
            return;
        }
        //surname alanı zorunlu olmayabilirdi.
        if (!validation.validateSurname(surname, inputLayoutSurname, userSurname)) {
            return;
        }
        if (!validation.validateEmail(email, inputLayoutEmail, userEmail)) {
            return;
        }
        if (!validation.validatePassword(password, inputLayoutPassword, userPassword)) {
            return;
        }
        Util.hideKeyboard(this);
        String userId = reference.push().getKey();
        User user = new User(userId, name, surname, email, password);
        registerUpdatePresenter.performSignUp(this, user);
    }

    @Override
    public void registerSuccess() {
        toast(this, getResources().getString(R.string.success_register));
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void registerError() {
        toast(this, getResources().getString(R.string.error_register));
    }

    @Override
    public void registerError(boolean emailCheck) {
        if (emailCheck) {
            toast(this, getResources().getString(R.string.error_register_email));
        }
    }

    public class RegisterTextWatcher implements TextWatcher {
        private View view;

        public RegisterTextWatcher(View view) {
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
