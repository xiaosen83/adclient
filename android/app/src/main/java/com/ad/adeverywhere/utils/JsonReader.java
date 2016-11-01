package com.ad.adeverywhere.utils;

/**
 * Created by cwang on 9/22/16.
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ad.adeverywhere.dao.Ad;

public class JsonReader {

    public static List<Ad> getAds(JSONObject jsonObject)
            throws JSONException {
        List<Ad> products = new ArrayList<Ad>();

        JSONObject data = jsonObject.getJSONObject("data");
        if(data == null)
            return null;
        JSONArray jsonArray = data.getJSONArray(Ad.AD_TAG_ADS);
        Ad product;
        for (int i = 0; i < jsonArray.length(); i++) {
            product = new Ad();
            JSONObject productObj = jsonArray.getJSONObject(i);
            product.setId(productObj.getString(Ad.AD_TAG_ID));
            product.setCompany(productObj.getString(Ad.AD_TAG_COMPANY));
            product.setAdDescription(productObj.getString(Ad.AD_TAG_DESCRIPTION));
            product.setLogoUri(productObj.getString(Ad.AD_TAG_LOGOURI));
            product.setAdUri(productObj.getString(Ad.AD_TAG_ADURI));
            product.setCarTotal(productObj.getInt(Ad.AD_TAG_CAR_TOTAL));
            product.setCarLeft(productObj.getInt(Ad.AD_TAG_CAR_LEFT));
            product.setMonth(productObj.getInt(Ad.AD_TAG_PERIOD));
            products.add(product);
        }
        return products;
    }
}