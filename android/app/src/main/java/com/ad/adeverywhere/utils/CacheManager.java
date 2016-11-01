package com.ad.adeverywhere.utils;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cwang on 9/22/16.
 */
public class CacheManager {
    private static CacheManager ourInstance = new CacheManager();

    public static CacheManager getInstance() {
        return ourInstance;
    }

    private CacheManager() {
    }

    private Map<String, Bitmap> mImageList = new HashMap<String, Bitmap>();

    public void addImage(String uri, Bitmap image){
        mImageList.put(uri, image);
    }

    public Bitmap getImage(String uri){
        return mImageList.get(uri);
    }

    public void ClearCache(){
        mImageList.clear();
    }
}
