package com.ad.adeverywhere.utils;

import android.media.Image;

/**
 * Created by cwang on 9/22/16.
 */
public class CacheItemImage {
    public String mUri = "";
    public Image mImage = null;

    public CacheItemImage(String uri, Image image){
        mUri = uri;
        mImage = image;
    }
}
