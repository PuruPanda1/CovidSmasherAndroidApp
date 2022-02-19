package com.purupanda.login.models;

import java.util.ArrayList;

public class vaccinationCenters {
    private String centerName,stateName,districtName,fee,age,vaccineName,from,to;
    private String pincode,centerId, capacity;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public vaccinationCenters(String centerName, String stateName, String districtName, String fee, String age, String vaccineName, String from, String to, String pincode, String centerId, String capacity) {
        this.centerName = centerName;
        this.stateName = stateName;
        this.districtName = districtName;
        this.fee = fee;
        this.age = age;
        this.vaccineName = vaccineName;
        this.from = from;
        this.to = to;
        this.pincode = pincode;
        this.centerId = centerId;
        this.capacity = capacity;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

}
