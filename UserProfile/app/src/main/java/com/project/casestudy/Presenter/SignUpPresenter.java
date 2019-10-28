package com.project.casestudy.Presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.casestudy.Constants;
import com.project.casestudy.Model.User;
import com.project.casestudy.Util;
import com.project.casestudy.View.SignUpView;

public class SignUpPresenter implements ISignUpPresenter {
    private SignUpView signUpView;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;

    public SignUpPresenter(SignUpView signUpView, FirebaseDatabase database, DatabaseReference reference, ProgressDialog progressDialog) {
        this.signUpView = signUpView;
        this.database = database;
        this.reference = reference;
        this.progressDialog = progressDialog;
    }

    @Override
    public void performSignUp(final Activity activity, final User user) {
        progressDialog.show();
        Util.setProgressDialog(progressDialog);
        Query queryData = reference.orderByChild(Constants.USER_EMAIL).equalTo(user.getUserEmail());
        queryData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    reference.child(user.getUserId()).setValue(user);
                    signUpView.registerSuccess();
                } else {
                    signUpView.registerError(true);
                }
                Util.dismisProgressDialog(progressDialog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(activity.getClass().getName(), databaseError.toString());
                signUpView.registerError();
                Util.dismisProgressDialog(progressDialog);
            }
        });
    }

}
