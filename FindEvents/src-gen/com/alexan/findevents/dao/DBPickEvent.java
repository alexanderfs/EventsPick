package com.alexan.findevents.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DBPICK_EVENT.
 */
public class DBPickEvent {

    private Long id;
    private Long userID;
    private String author;
    private String title;
    private String description;
    private String address;
    private String province;
    private String city;
    private String addressdetail;
    private String photo;
    private String startt;
    private String endt;
    private String startd;
    private String endd;
    private Integer collectionNum;
    private Integer attendNum;
    private Integer commentNum;
    private String catagory;

    public DBPickEvent() {
    }

    public DBPickEvent(Long id) {
        this.id = id;
    }

    public DBPickEvent(Long id, Long userID, String author, String title, String description, String address, String province, String city, String addressdetail, String photo, String startt, String endt, String startd, String endd, Integer collectionNum, Integer attendNum, Integer commentNum, String catagory) {
        this.id = id;
        this.userID = userID;
        this.author = author;
        this.title = title;
        this.description = description;
        this.address = address;
        this.province = province;
        this.city = city;
        this.addressdetail = addressdetail;
        this.photo = photo;
        this.startt = startt;
        this.endt = endt;
        this.startd = startd;
        this.endd = endd;
        this.collectionNum = collectionNum;
        this.attendNum = attendNum;
        this.commentNum = commentNum;
        this.catagory = catagory;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressdetail() {
        return addressdetail;
    }

    public void setAddressdetail(String addressdetail) {
        this.addressdetail = addressdetail;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStartt() {
        return startt;
    }

    public void setStartt(String startt) {
        this.startt = startt;
    }

    public String getEndt() {
        return endt;
    }

    public void setEndt(String endt) {
        this.endt = endt;
    }

    public String getStartd() {
        return startd;
    }

    public void setStartd(String startd) {
        this.startd = startd;
    }

    public String getEndd() {
        return endd;
    }

    public void setEndd(String endd) {
        this.endd = endd;
    }

    public Integer getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(Integer collectionNum) {
        this.collectionNum = collectionNum;
    }

    public Integer getAttendNum() {
        return attendNum;
    }

    public void setAttendNum(Integer attendNum) {
        this.attendNum = attendNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

}
