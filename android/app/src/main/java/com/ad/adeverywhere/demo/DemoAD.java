package com.ad.adeverywhere.demo;

import com.ad.adeverywhere.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cwang on 10/9/16.
 */

public class DemoAD {
    public static final int AD_STATUS_PENDING = 0;
    public static final int AD_STATUS_READY = 1;
    public static final int AD_STATUS_FINISHED = 2;

    private long id;
    private int cars;
    private int carsNow;
    private String company = "";
    private String startDate = "";
    private String endDate = "";
    private String logo = "";
    private String model = "";
    private long userId;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getCars() {
        return cars;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLogo() {
        return logo;
    }

    public String getModel() {
        return model;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setCars(int cars) {
        this.cars = cars;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getCarsNow() {
        return carsNow;
    }

    public void setCarsNow(int carsNow) {
        this.carsNow = carsNow;
    }

    public int getPeriod(){
        //compute period in month = date_end - dates_tart
        String[] startS = startDate.split("-");
        String[] endS = endDate.split("-");
        int month = 0;
        if(startS.length == 3 || endS.length == 3){
            month = (Integer.parseInt(endS[0])-Integer.parseInt(startS[0]))*12
                    +(Integer.parseInt(endS[1])-Integer.parseInt(startS[1]));
        }
        return month;
    }

    //date format "2016-09-12"
    public int getStatus(){
        int status = AD_STATUS_PENDING;
        Calendar date = Calendar.getInstance();
        String[] startS = startDate.split("-");
        String[] endS = endDate.split("-");
        Calendar start = (Calendar)date.clone();
        Calendar end = (Calendar)date.clone();
        if(startS.length == 3 && endS.length == 3){
            start.set(Calendar.YEAR, Integer.parseInt(startS[0]));
            start.set(Calendar.MONTH, Integer.parseInt(startS[1]));
            start.set(Calendar.DATE, Integer.parseInt(startS[2]));
            end.set(Calendar.YEAR, Integer.parseInt(endS[0]));end.set(Calendar.MONTH, Integer.parseInt(endS[1]));end.set(Calendar.DATE, Integer.parseInt(endS[2]));
            if(date.before(start))
                status = AD_STATUS_PENDING;
            else  if(date.before(end))
                status = AD_STATUS_READY;
            else
                status = AD_STATUS_FINISHED;
        }
        return status;
    }
}
