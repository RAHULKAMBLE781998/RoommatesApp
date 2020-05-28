package com.study.studyspace;

import java.io.Serializable;

public class Roommate implements Serializable {
    String username;
    String userID;
    String branch;
    String year;
    String collegeid;
    String raddid; //addID
    String needed; //requirements
    String gender;
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }


    public void setNeeded(String needed) {
        this.needed = needed;
    }

    public String getNeeded() {
        return needed;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCollegeid(String collegeid) {
        this.collegeid = collegeid;
    }

    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }

    public String getBranch() {
        return branch;
    }

    public String getYear() {
        return year;
    }

    public String getCollegeid() {
        return collegeid;
    }

    public void setRAddID(String raddid) {
        this.raddid = raddid;
    }
    public String getRAddID() {
        return raddid;
    }
}
