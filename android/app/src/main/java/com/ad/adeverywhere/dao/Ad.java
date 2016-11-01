package com.ad.adeverywhere.dao;

/**
 * Created by cwang on 9/19/16.
 */
public class Ad {
    public static final String AD_TAG_ADS = "ads";
    public static final String AD_TAG_ID = "id";
    public static final String AD_TAG_COMPANY = "company";
    public static final String AD_TAG_LOGOURI = "logo_uri";
    public static final String AD_TAG_ADURI = "ad_uri";
    public static final String AD_TAG_CAR_TOTAL = "total";
    public static final String AD_TAG_CAR_LEFT = "left";
    public static final String AD_TAG_DESCRIPTION = "description";
    public static final String AD_TAG_PERIOD = "period";

    private String id = "";
    private String company = "";
    private String adDescription = "";
    private Integer carTotal = 0;
    private Integer carLeft = 0;
    private String logoUri = "";
    private String adUri = "";
    private String startDate = "";
    private String endDate = "";
    private Integer month = 0;

    public String getId() {
        return id;
    }
    public void setId(String idd){
        id = idd;
    }

    public Integer getCarLeft() {
        return carLeft;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getCarTotal() {
        return carTotal;
    }

    public String getAdDescription() {
        return adDescription;
    }

    public String getAdUri() {
        return adUri;
    }

    public String getCompany() {
        return company;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    public void setAdUri(String adUri) {
        this.adUri = adUri;
    }

    public void setCarLeft(Integer carLeft) {
        this.carLeft = carLeft;
    }

    public void setCarTotal(Integer carTotal) {
        this.carTotal = carTotal;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
