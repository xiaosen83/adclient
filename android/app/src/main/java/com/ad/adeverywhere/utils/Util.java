package com.ad.adeverywhere.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.demo.DemoAD;

/**
 * Created by cwang on 10/10/16.
 */

public class Util {

    public static int getResourceId(String pResourceName, Class<?> c){
        try{
            Field idField = c.getDeclaredField(pResourceName.substring(0, pResourceName.length()-4));
            return idField.getInt(idField);
        }catch(Exception e){
            try {
                Field idField = R.drawable.class.getDeclaredField("arrow");
                return idField.getInt(idField);
            }catch(Exception ee){
            }
        }
        return 0;
    }

    public static String statusToString(Context context, int status){
        if(status == DemoAD.AD_STATUS_PENDING)
            return context.getString(R.string.ad_status_pending);
        else if(status == DemoAD.AD_STATUS_READY)
            return context.getString(R.string.ad_status_ready);
        else if(status == DemoAD.AD_STATUS_FINISHED)
            return context.getString(R.string.ad_status_finished);
        return "";
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

        // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static long calStringToLong(String strDate){
        Calendar date = Calendar.getInstance();
        String[] startS = strDate.split("-");
        if(startS.length == 3) {
            date.set(Calendar.YEAR, Integer.parseInt(startS[0]));
            date.set(Calendar.MONTH, Integer.parseInt(startS[1]));
            date.set(Calendar.DATE, Integer.parseInt(startS[2]));
        }
        return date.getTimeInMillis();
    }

    public static String calLongToString(long timeinmillis){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dateFormatter.format(timeinmillis);
    }

    public static int getDaysOfPeriod(Context context, String period){
        int days = 30;
        String[] periods = context.getResources().getStringArray(R.array.client_ad_period);
        for(int i=0; i<periods.length;i++){
            if(period.equals(periods[i])){
                switch (i){
                    case 0:
                        days = 30;
                        break;
                    case 1:
                        days = 90;break;
                    case 2:
                        days = 180;break;
                    case 3:
                        days = 270;break;
                    case 4:
                        days = 360;break;
                    default:
                        days = 30;break;
                }
                break;
            }
        }
        return days;
    }
}
