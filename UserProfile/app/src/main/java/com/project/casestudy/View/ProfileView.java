package com.project.casestudy.View;

import com.project.casestudy.Model.User;

public interface ProfileView {
    void updateSuccess();
    void updateError();
    void deleteError();
    void deteteSuccess();
    void retrieveUser(User user);
}
