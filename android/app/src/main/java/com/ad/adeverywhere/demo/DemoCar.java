package com.ad.adeverywhere.demo;

/**
 * Created by cwang on 10/9/16.
 */

public class DemoCar {
    private long id;
    private String plate;
    private String brand;
    private String model;
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPlate() {
        return plate;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
}
