package com.ad.adeverywhere.demo;

/**
 * Created by cwang on 10/9/16.
 */

public class DemoUser {
    private long id;
    private String name = "";
    private String type = "";
    private String passwd = "";
    private String model = "";
    private String addr = "";
    private String phone = "";
    private String email = "";

    public void setId(long id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getModel() {
        return model;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddr() {
        return addr;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
