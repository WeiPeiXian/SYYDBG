package com.example.weipeixian.MYYDBG.model;

public class PhoneInfo {

    private String name;        //联系人姓名

    private String telPhone;    //电话号码
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getName() {

        return name;

    }



    public void setName(String name) {

        this.name = name;

    }



    public String getTelPhone() {

        return telPhone;

    }



    public void setTelPhone(String telPhone) {

        this.telPhone = telPhone;

    }



    public PhoneInfo() {

    }



    public PhoneInfo(String name, String telPhone) {

        this.name = name;

        this.telPhone = telPhone;

    }

}
