package com.ad.adeverywhere.utils;

/**
 * Created by cwang on 9/22/16.
 */
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;

public class GetJSONObject {

    public static JSONObject getJSONObject(String url) throws IOException,
            JSONException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        // Use HttpURLConnection
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            //jsonObject = jsonParser.getJSONHttpURLConnection(url);
            //TODO: for testing purpose, using dummy data
            jsonObject = jsonParser.getJSONHttpURLConnectionDummy(url);
        } else {
            // use HttpClient
            //jsonObject = jsonParser.getJSONHttpClient(url);
        }
        return jsonObject;
    }
}