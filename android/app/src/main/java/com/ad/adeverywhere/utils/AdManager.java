package com.ad.adeverywhere.utils;

import com.ad.adeverywhere.dao.Ad;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwang on 9/22/16.
 */
public class AdManager {
    public static final String TAG = "AdManager";
    public static final Logger logger = Logger.getInstance();

    public static final String URI_DOWNLOAD_URI = "http://10.103.62.201/download/ads";

    private List<Ad> mAdList = null;

    private static AdManager ourInstance = new AdManager();
    public static AdManager getInstance() {
        return ourInstance;
    }
    private AdManager() {
        mAdList = new ArrayList<Ad>();
    }

    public Ad getAdByName(String company)
    {
        for(int i=0; i<mAdList.size(); i++)
        {
            if(mAdList.get(i).getCompany().equals(company))
                return mAdList.get(i);
        }
        return null;
    }

    public List<Ad> getAll(){
        return mAdList;
    }

    public void loadAds(){
        //TODO: download data from server
        loadAdsDummy();
    }

    public void loadAdsDummy(){
        try {
            JSONObject json = GetJSONObject.getJSONObject(URI_DOWNLOAD_URI);
            mAdList = JsonReader.getAds(json);
        }catch (Exception e){
            logger.logError(TAG, "Parse JSON failed:"+ e.toString());
        }
    }
    public Ad findById(String id){
        if(id != null && !id.equals("")) {
            for (int i = 0; i < mAdList.size(); i++) {
                if (id.equals(mAdList.get(i).getId()))
                    return mAdList.get(i);
            }
        }
        //return a dummy Ad
        Ad ad = new Ad();
        ad.setCompany("test_company");
        ad.setAdDescription("test_description");
        ad.setCarTotal(500);
        ad.setCarLeft(100);
        ad.setLogoUri("http://download.easyicon.net/png/511332/48/");
        ad.setAdUri("http://d5.sina.com.cn/pfpghc2/201609/18/87e9a10c82c9469a88d89ff4249c1f10.jpg");
        return ad;
    }
}
