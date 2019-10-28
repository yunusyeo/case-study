package com.project.casestudy.Presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.casestudy.Model.User;
import com.project.casestudy.Util;
import com.project.casestudy.View.LoginView;

public class LoginPresenter implements ILoginPresenter {
    private LoginView loginView;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;

    public LoginPresenter(LoginView loginView, FirebaseDatabase database, DatabaseReference reference, ProgressDialog progressDialog) {
        this.loginView = loginView;
        this.database = database;
        this.reference = reference;
        this.progressDialog = progressDialog;
    }

    @Override
    public void performLogin(final Activity activity, final String userEmail, final String userPassword) {
        progressDialog.show();
        Util.setProgressDialog(progressDialog);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if (user.getUserEmail().equals(userEmail) && user.getUserPassword().equals(userPassword)) {
                        loginView.loginSuccess(user.getUserId());
                        Util.dismisProgressDialog(progressDialog);
                        return;
                    }
                }
                loginView.loginError();
                Util.dismisProgressDialog(progressDialog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(activity.getClass().getName(), databaseError.toString());
                loginView.loginError();
                Util.dismisProgressDialog(progressDialog);
            }
        });
    }
}
