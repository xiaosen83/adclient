package com.ad.adeverywhere.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class JSONParser {
    public static final String TAG = "JSONParser";
    public static final Logger logger = Logger.getInstance();

    static String json = "";

    private HttpURLConnection httpConnection;

    InputStream inputStream = null;
    BufferedReader reader = null;

    public JSONParser() {
    }

    private void openHttpUrlConnection(String urlString) throws IOException {
        Log.d("urlstring in parser", urlString+"");
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        httpConnection = (HttpURLConnection) connection;
        httpConnection.setConnectTimeout(30000);
        httpConnection.setRequestMethod("GET");

        httpConnection.connect();
    }
/*
    private void openHttpClient(String urlString) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpParams httpParams = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);

        HttpGet httpGet = new HttpGet(urlString);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        inputStream = httpEntity.getContent();

        reader = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8"), 8);
    }

    // using HttpClient for <= Froyo
    public JSONObject getJSONHttpClient(String url)
            throws ClientProtocolException, IOException, JSONException {
        JSONObject jsonObject = null;
        try {
            openHttpClient(url);

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            json = sb.toString();

            Log.d("json", json);
            jsonObject = new JSONObject(json);

        } finally {
            FileUtils.close(inputStream);
            FileUtils.close(reader);
        }
        return jsonObject;
    }
    */

    // using HttpURLConnection for > Froyo
    public JSONObject getJSONHttpURLConnection(String urlString)
            throws IOException, JSONException {

        BufferedReader reader = null;
        StringBuffer output = new StringBuffer("");
        InputStream stream = null;
        JSONObject jsonObject = null;
        try {

            openHttpUrlConnection(urlString);

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream,
                        "UTF-8"), 8);
                String line = "";
                while ((line = reader.readLine()) != null)
                    output.append(line + "\n");
                json = output.toString();
                jsonObject = new JSONObject(json);
            }

        } finally {
            stream.close();
            reader.close();
        }
        return jsonObject;
    }

    public JSONObject getJSONHttpURLConnectionDummy(String urlString) {
        String ads_json =
                "{"
                    + "\"status\": true,"
                    + "\"data\": {"
                    + "\"ads\": ["
                            + "{"
                                + "\"id\": \"111\","
                                + "\"company\": \"Google\","
                                + "\"description\": \"Google want's do drop AD on your car\","
                                + "\"logo_uri\": \"http://download.easyicon.net/png/511332/48/\","
                                + "\"ad_uri\": \"http://www.jdxc.sh.cn/data/attachment/portal/201510/27/165414bjo0g7zqdoaoga5a.jpg\","
                                + "\"total\": 500,"
                                + "\"left\": 200,"
                                + "\"period\": 3,"
                                + "\"start_date\": \"2016-09-01\","
                                + "\"end_date\": \"2016-11-01\""
                            + "},"
                            + "{"
                                + "\"id\": \"2222\","
                                + "\"company\": \"Microsoft\","
                                + "\"description\": \"Google want's do drop AD on your car\","
                                + "\"logo_uri\": \"http://download.easyicon.net/png/511332/48/\","
                                + "\"ad_uri\": \"http://d1.leju.com/ia/2016/09/13/f57d7bc34859baimg.jpg\","
                                + "\"total\": 500,"
                                + "\"left\": 200,"
                                + "\"period\": 3,"
                                + "\"start_date\": \"2016-09-01\","
                                + "\"end_date\": \"2016-11-01\""
                            + "},"
                            + "{"
                                + "\"id\": \"33333\","
                                + "\"company\": \"Apple\","
                                + "\"description\": \"Google want's do drop AD on your car\","
                                + "\"logo_uri\": \"http://download.easyicon.net/png/511332/48/\","
                                + "\"ad_uri\": \"http://d5.sina.com.cn/pfpghc2/201609/18/87e9a10c82c9469a88d89ff4249c1f10.jpg\","
                                + "\"total\": 500,"
                                + "\"left\": 200,"
                                + "\"period\": 3,"
                                + "\"start_date\": \"2016-09-01\","
                                + "\"end_date\": \"2016-11-01\""
                            + "}"
                    + "]"
                    + "}"
                + "}";

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(ads_json);
        }catch (JSONException e){
            logger.logError(TAG, e.toString());
        }
        return jsonObject;
    }
}