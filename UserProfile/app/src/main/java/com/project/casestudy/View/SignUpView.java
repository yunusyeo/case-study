package com.project.casestudy.View;

public interface SignUpView {
    void registerSuccess();
    void registerError();
    void registerError(boolean emailCheck);
}
