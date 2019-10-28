package com.project.casestudy.Model;

public class User {
    private String userId;
    private String userName;
    private String userSurName;
    private String userEmail;
    private String userPassword;

    public User() {
    }

    public User(String userId, String userName, String userSurName, String userEmail, String userPassword) {
        this.userId = userId;
        this.userName = userName;
        this.userSurName = userSurName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurName() {
        return userSurName;
    }

    public void setUserSurName(String userSurName) {
        this.userSurName = userSurName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
