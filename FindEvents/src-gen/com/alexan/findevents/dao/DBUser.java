package com.alexan.findevents.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DBUSER.
 */
public class DBUser {

    private Long id;
    private Long userID;
    private String phoneNumber;
    private String emailAddr;
    private String username;
    private Boolean gender;
    private String signature;
    private Long timestamp;

    public DBUser() {
    }

    public DBUser(Long id) {
        this.id = id;
    }

    public DBUser(Long id, Long userID, String phoneNumber, String emailAddr, String username, Boolean gender, String signature, Long timestamp) {
        this.id = id;
        this.userID = userID;
        this.phoneNumber = phoneNumber;
        this.emailAddr = emailAddr;
        this.username = username;
        this.gender = gender;
        this.signature = signature;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
