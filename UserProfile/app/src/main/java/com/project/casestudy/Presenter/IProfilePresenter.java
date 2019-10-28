package com.project.casestudy.Presenter;

import android.app.Activity;

import com.project.casestudy.Model.User;

public interface IProfilePresenter {
    void updateUser(Activity activity, User user);
    void retrieveUser(Activity activity, String userId);
    void deleteUser(Activity activity, String userId);
}
