package com.project.casestudy.Presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.casestudy.Model.User;
import com.project.casestudy.Util;
import com.project.casestudy.View.ProfileView;

public class ProfilePresenter implements IProfilePresenter {
    private ProfileView profileView;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;

    public ProfilePresenter(ProfileView profileView, FirebaseDatabase database, DatabaseReference reference, ProgressDialog progressDialog) {
        this.profileView = profileView;
        this.database = database;
        this.reference = reference;
        this.progressDialog = progressDialog;
    }

    @Override
    public void updateUser(Activity activity, User user) {
        progressDialog.show();
        Util.setProgressDialog(progressDialog);
        reference.child(user.getUserId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    profileView.updateSuccess();
                } else {
                    profileView.updateError();
                }
                Util.dismisProgressDialog(progressDialog);
            }
        });
    }

    @Override
    public void retrieveUser(final Activity activity, String userId) {
        progressDialog.show();
        Util.setProgressDialog(progressDialog);
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                profileView.retrieveUser(user);
                Util.dismisProgressDialog(progressDialog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(activity.getClass().getName(), databaseError.toString());
                Util.dismisProgressDialog(progressDialog);
            }
        });
    }

    @Override
    public void deleteUser(Activity activity, String userId) {
        progressDialog.show();
        Util.setProgressDialog(progressDialog);
        reference.child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    profileView.deteteSuccess();
                } else {
                    profileView.deleteError();
                }
                Util.dismisProgressDialog(progressDialog);
            }
        });
    }

}
