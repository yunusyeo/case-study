package com.project.casestudy.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.casestudy.Constants;
import com.project.casestudy.CustomDialogFragment;
import com.project.casestudy.Model.User;
import com.project.casestudy.Presenter.ProfilePresenter;
import com.project.casestudy.R;
import com.project.casestudy.Util;
import com.project.casestudy.Validation;
import com.project.casestudy.View.ProfileView;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, ProfileView, CustomDialogFragment.DecideDialogFragment {
    private static final String LOGIN_CHECK = "LOGIN_CHECK";
    private static final String MESSAGE = "MESSAGE";
    private static final String TEXT = "TEXT";
    private static final String USER_ID = "USER_ID";
    private String updateTag = "update";
    private String editTag = "edit";
    private Toolbar toolbar;
    private TextInputLayout inputLayoutName, inputLayoutSurname, inputLayoutEmail, inputLayoutPassword;
    private EditText userName;
    private EditText userSurname;
    private EditText userEmail;
    private EditText userPassword;
    private Button update, deleteMember, logout;
    private String name;
    private String surname;
    private String email;
    private String password;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ProgressDialog progressDialog;
    private ProfilePresenter profilePresenter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Validation validation;
    private boolean loginCheck;
    private String userId;

    public static Intent newIntent(Activity activity, String userId, boolean loginCheck) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra(LOGIN_CHECK, loginCheck);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.profile_text));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString(USER_ID, "");
            loginCheck = bundle.getBoolean(LOGIN_CHECK, false);
        }

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutSurname = (TextInputLayout) findViewById(R.id.input_layout_surname);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        userName = (EditText) findViewById(R.id.input_name);
        userSurname = (EditText) findViewById(R.id.input_surname);
        userEmail = (EditText) findViewById(R.id.input_email);
        userPassword = (EditText) findViewById(R.id.input_password);
        update = (Button) findViewById(R.id.update);
        deleteMember = (Button) findViewById(R.id.delete);
        logout = (Button) findViewById(R.id.logout);

        userEmail.addTextChangedListener(new ProfileTextWatcher(userEmail));
        userPassword.addTextChangedListener(new ProfileTextWatcher(userPassword));

        update.setOnClickListener(this);
        deleteMember.setOnClickListener(this);
        logout.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        validation = new Validation(this);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");
        profilePresenter = new ProfilePresenter(this, database, reference,progressDialog);

        if (loginCheck) {
            profilePresenter.retrieveUser(this, userId);
            update.setText(getResources().getString(R.string.profile_edit));
            update.setTag(editTag);
            userName.setEnabled(false);
            userSurname.setEnabled(false);
            userEmail.setEnabled(false);
            userPassword.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                if (loginCheck && update.getTag().equals(editTag)) {
                    update.setText(getResources().getString(R.string.profile_update));
                    userName.setEnabled(true);
                    userSurname.setEnabled(true);
                    userEmail.setEnabled(true);
                    userPassword.setEnabled(true);
                    update.setTag(updateTag);
                } else {
                    if (Util.isNetworkConnected(this)) {
                        name = userName.getText().toString().trim();
                        surname = userSurname.getText().toString().trim();
                        email = userEmail.getText().toString().trim();
                        password = userPassword.getText().toString().trim();
                        submit(name, surname, email, password);
                    } else {
                        toast(this, getResources().getString(R.string.error_internet_connected));
                    }
                }
                break;
            case R.id.delete:
                if (Util.isNetworkConnected(this)) {
                    CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(MESSAGE, getResources().getString(R.string.warning_text));
                    bundle.putString(TEXT, Constants.DELETE);
                    customDialogFragment.setArguments(bundle);
                    customDialogFragment.show(fragmentManager, "DF");
                } else {
                    toast(this, getResources().getString(R.string.error_internet_connected));
                }
                break;
            case R.id.logout:
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MESSAGE, getResources().getString(R.string.logout_text));
                bundle.putString(TEXT, Constants.EXIT);
                customDialogFragment.setArguments(bundle);
                customDialogFragment.show(fragmentManager, "DF");
                break;
        }
    }

    private void submit(String name, String surname, String email, String password) {
        if (!validation.validateName(name, inputLayoutName, userName)) {
            return;
        }
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

        User user = new User(userId, name, surname, email, password);
        profilePresenter.updateUser(this, user);
    }

    @Override
    public void updateSuccess() {
        toast(this, getResources().getString(R.string.success_update));

    }

    @Override
    public void updateError() {
        toast(this, getResources().getString(R.string.error_update));
    }

    @Override
    public void deteteSuccess() {
        toast(this, getResources().getString(R.string.success_delete));

        SharedPreferences preferences = this.getSharedPreferences(Constants.USER_ID, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Constants.USER_ID);
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void deleteError() {
        toast(this, getResources().getString(R.string.error_delete));
    }


    @Override
    public void retrieveUser(User user) {
        if (user != null) {
            userName.setText(user.getUserName());
            userSurname.setText(user.getUserSurName());
            userEmail.setText(user.getUserEmail());
            userPassword.setText(user.getUserPassword());
        }
    }

    @Override
    public void onDecideDialogFragment(String text) {
        if (text.equals(Constants.DELETE)) {
            profilePresenter.deleteUser(this, userId);

        } else {
            SharedPreferences preferences = this.getSharedPreferences(Constants.USER_ID, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(Constants.USER_ID);
            editor.commit();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public class ProfileTextWatcher implements TextWatcher {
        private View view;

        public ProfileTextWatcher(View view) {
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
