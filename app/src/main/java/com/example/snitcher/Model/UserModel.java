package com.example.snitcher.Model;

import java.io.Serializable;

public class UserModel implements Serializable {
    String profilepic,mail,userName,password,userId,lastMessage,Status,bio,Dob;

    public UserModel(String userName) {
        this.userName = userName;
    }

    public UserModel(String profilepic, String mail, String userName, String password, String userId, String lastMessage, String status, String bio, String dob) {
        this.profilepic = profilepic;
        this.mail = mail;
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        Status = status;
        this.bio = bio;
        Dob = dob;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public UserModel(String mail, String userName, String password) {
        this.mail = mail;
        this.userName = userName;
        this.password = password;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public UserModel() {
    }

    public UserModel(String profilepic, String mail, String userName, String password, String userId, String lastMessage, String status) {
        this.profilepic = profilepic;
        this.mail = mail;
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        Status = status;
    }
}
