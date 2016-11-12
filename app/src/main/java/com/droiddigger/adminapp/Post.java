package com.droiddigger.adminapp;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mufad on 11/11/2016.
 */

public class Post implements Serializable{
    String title, imageUrl, userName, etDay, status, lat, longt, timestamp, solveDate, key, solution, urgency;

    public Post() {
    }

    public Post(String title, String imageUrl, String userName, String etDay, String status, String lat, String longt, String timestamp, String solveDate, String key, String solution, String urgency) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.etDay = etDay;
        this.status = status;
        this.lat = lat;
        this.longt = longt;
        this.timestamp = timestamp;
        this.solveDate = solveDate;
        this.key = key;
        this.solution=solution;
        this.urgency=urgency;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEtDay() {
        return etDay;
    }

    public void setEtDay(String etDay) {
        this.etDay = etDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSolveDate() {
        return solveDate;
    }

    public void setSolveDate(String solveDate) {
        this.solveDate = solveDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }


}
