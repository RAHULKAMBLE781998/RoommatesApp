package com.study.studyspace;


import java.io.Serializable;

public class Add implements Serializable {
    private String title;
    private String price;
    private String address;
    private String description;
    private String accomodationtype;
    private String accomodationfor;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String addID;
    private String username;
    private String userid;
    private String uploader;
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public String getUserid() {
        return userid;
    }

    public void setClgid(String clgid) {
        this.clgid = clgid;
    }

    public String getClgid() {
        return clgid;
    }

    private String clgid;

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getUploader() {
        return uploader;
    }



    public void setAddID(String addID) {
        this.addID = addID;
    }

    public String getAddID() {
        return addID;
    }

    public String getImg1() {
        return img1;
    }

    public String getImg2() {
        return img2;
    }

    public String getImg3() {
        return img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public void setAccomodationfor(String accomodationfor) {
        this.accomodationfor = accomodationfor;
    }

    public String getAccomodationfor() {
        return accomodationfor;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAccomodationtype(String accomodationtype) {
        this.accomodationtype = accomodationtype;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getAccomodationtype() {
        return accomodationtype;
    }

}

