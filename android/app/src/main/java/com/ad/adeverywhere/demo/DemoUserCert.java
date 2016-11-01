package com.ad.adeverywhere.demo;

/**
 * Created by cwang on 10/20/16.
 */

public class DemoUserCert {
    private String name = "";
    private String identity="";
    private String city = "";
    private int userId = -1;
    private byte[] photo = null;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public String getIdentity() {
        return identity;
    }

    public String getName() {
        return name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
