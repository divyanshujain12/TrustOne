package com.example.deii.Models;

/**
 * Created by deii on 10/14/2015.
 */
public class UserDetailModel {

    public String username;
    public String emailid;
    public String profileimage;
    public String phonenumber;

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        username = userName;
    }

    public String getEmailID() {
        return emailid;
    }

    public void setEmailID(String emailID) {
        emailid = emailID;
    }

    public String getProfileImage() {
        return profileimage;
    }

    public void setProfileImage(String profileImage) {
        profileimage = profileImage;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        phonenumber = phoneNumber;
    }
}
