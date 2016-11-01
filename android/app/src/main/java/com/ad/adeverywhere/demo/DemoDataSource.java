package com.ad.adeverywhere.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.utils.Logger;
import com.ad.adeverywhere.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.ad.adeverywhere.demo.DBHelper.AD_TYPE_FINISHED;
import static com.ad.adeverywhere.demo.DBHelper.AD_TYPE_PENDING;
import static com.ad.adeverywhere.demo.DBHelper.AD_TYPE_READY;
import static com.ad.adeverywhere.demo.DBHelper.TABLE_AD;

/**
 * Created by cwang on 10/10/16.
 */

public class DemoDataSource {
    public static final String TAG = "DBHelper";
    private static final Logger logger = Logger.getInstance();

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public DemoDataSource(Context context){
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException{
        db = dbHelper.getWritableDatabase();
    }
    public void close(){
        db.close();
        dbHelper.close();
    }
    public List<DemoAD> getAds(int id){
        List<DemoAD> ads = new ArrayList<>();
        Cursor res = null;
        if(id <= 0)
            res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " ", null);
        else
            res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE id = " +id+ " ", null);
        res.moveToFirst();
        while(!res.isAfterLast())
        {
            DemoAD ad = cursorToAD(res);
            ads.add(ad);
            res.moveToNext();
        }
        res.close();
        return ads;
    }

    public List<DemoAD> getAdsNotBelongToUser(int user_id){
        if(user_id <=0)
            return null;
        List<DemoAD> ads = new ArrayList<>();
        Cursor res = null;
        String query = String.format("SELECT * FROM ad WHERE id not in (SELECT ad_id FROM "+DBHelper.TABLE_AD_CAR+" WHERE car_id in (SELECT id FROM car WHERE user_id=%s));", user_id);
        res = db.rawQuery(query, null);
        res.moveToFirst();
        while(!res.isAfterLast())
        {
            DemoAD ad = cursorToAD(res);
            ads.add(ad);
            res.moveToNext();
        }
        res.close();
        return ads;
    }

    public List<DemoAD> getAdsOfClient(int user_id){
        List<DemoAD> ads = new ArrayList<>();
        Cursor res = null;
        res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE user_id = " +user_id+ ";", null);
        res.moveToFirst();
        while(!res.isAfterLast())
        {
            DemoAD ad = cursorToAD(res);
            ads.add(ad);
            res.moveToNext();
        }
        res.close();
        return ads;
    }

    public List<DemoAD> getDriverAds(int user_id, int type){
        Calendar now = Calendar.getInstance();
        List<DemoAD> ads = new ArrayList<>();
        Cursor res = null;
        res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE id IN (SELECT ad_id FROM " + DBHelper.TABLE_AD_CAR + " WHERE car_id IN (SELECT id FROM " + DBHelper.TABLE_CAR + " WHERE user_id=?));", new String[]{String.valueOf(user_id)});
        if (type == AD_TYPE_PENDING)
            res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE id IN (SELECT ad_id FROM " + DBHelper.TABLE_AD_CAR + " WHERE car_id IN (SELECT id FROM " + DBHelper.TABLE_CAR + " WHERE user_id=?) AND start_date > " + now.getTimeInMillis() + ");", new String[]{String.valueOf(user_id)});
        else if (type == AD_TYPE_READY)
            res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE id IN (SELECT ad_id FROM " + DBHelper.TABLE_AD_CAR + " WHERE car_id IN (SELECT id FROM " + DBHelper.TABLE_CAR + " WHERE user_id=?) AND start_date < " + now.getTimeInMillis() + " AND end_date > " + now.getTimeInMillis() + ");", new String[]{String.valueOf(user_id)});
        else if (type == AD_TYPE_FINISHED)
            res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE id IN (SELECT ad_id FROM " + DBHelper.TABLE_AD_CAR + " WHERE car_id IN (SELECT id FROM " + DBHelper.TABLE_CAR + " WHERE user_id=?) AND end_date < " + now.getTimeInMillis() + ");", new String[]{String.valueOf(user_id)});
        else
            res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE id IN (SELECT ad_id FROM " + DBHelper.TABLE_AD_CAR + " WHERE car_id IN (SELECT id FROM " + DBHelper.TABLE_CAR + " WHERE user_id=?));", new String[]{String.valueOf(user_id)});
        res.moveToFirst();
       while(!res.isAfterLast())
        {
            DemoAD ad = cursorToAD(res);
            ads.add(ad);
            res.moveToNext();
        }
        res.close();
        return ads;
    }

    public List<DemoAD> getClientAds(int user_id, int type){
        Calendar now = Calendar.getInstance();
        List<DemoAD> ads = new ArrayList<>();
        if(user_id>0) {
            Cursor res = null;
            if (type == AD_TYPE_PENDING)
                res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE user_id = " + user_id + " AND start_date > " + now.getTimeInMillis() + ";", null);
            else if (type == AD_TYPE_READY)
                res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE user_id = " + user_id + " AND start_date < " + now.getTimeInMillis() + " AND end_date > " + now.getTimeInMillis() + ";", null);
            else if (type == AD_TYPE_FINISHED)
                res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE user_id = " + user_id + " AND end_date < " + now.getTimeInMillis() + ";", null);
            else
                res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_AD + " WHERE user_id = " + user_id + ";", null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                DemoAD ad = cursorToAD(res);
                ads.add(ad);
                res.moveToNext();
            }
            res.close();
        }
        return ads;
    }

    public boolean createAD(DemoAD ad){
        ContentValues cv = new  ContentValues();
        cv.put(DBHelper.AD_COL_CARS,    ad.getCars());
        cv.put(DBHelper.AD_COL_CARS_NOW, 0);
        cv.put(DBHelper.AD_COL_COMPANY, ad.getCompany());
        cv.put(DBHelper.AD_COL_START_DATE,    Util.calStringToLong(ad.getStartDate()));
        cv.put(DBHelper.AD_COL_END_DATE,    Util.calStringToLong(ad.getEndDate()));
        cv.put(DBHelper.AD_COL_LOGO_URI,    ad.getLogo());
        cv.put(DBHelper.AD_COL_MODEL_URL,    ad.getModel());
        cv.put(DBHelper.AD_COL_USER_ID, ad.getUserId());
        long num = db.insert(DBHelper.TABLE_AD, null, cv);
        return num>0;
    }

    public boolean deleteAD(int adID){
        boolean ret = true;
        int num = db.delete(DBHelper.TABLE_AD, "id=?", new String[]{String.valueOf(adID)});
        return num==1;
    }

    public boolean updateAD(DemoAD ad){
        ContentValues cv = new  ContentValues();
        cv.put(DBHelper.AD_COL_CARS,    ad.getCars());
        //cv.put(DBHelper.AD_COL_CARS_NOW, 0);
        cv.put(DBHelper.AD_COL_COMPANY, ad.getCompany());
        cv.put(DBHelper.AD_COL_START_DATE,    Util.calStringToLong(ad.getStartDate()));
        cv.put(DBHelper.AD_COL_END_DATE,    Util.calStringToLong(ad.getEndDate()));
        cv.put(DBHelper.AD_COL_LOGO_URI,    ad.getLogo());
        cv.put(DBHelper.AD_COL_MODEL_URL,    ad.getModel());
        return db.update(DBHelper.TABLE_AD, cv, "id=?", new String[]{String.valueOf(ad.getId())}) > 0;
    }

    public boolean driverAlreadyHasAD(int userId, int adId){
        boolean has = false;
        Cursor res = null;
        try{
            res = db.rawQuery("SELECT * FROM "+DBHelper.TABLE_AD_CAR+" WHERE "+DBHelper.ADCAR_COL_ADID+"=? and "+DBHelper.ADCAR_COL_CARID+" in (SELECT id FROM car WHERE user_id=?);", new String[]{String.valueOf(adId), String.valueOf(userId)});
            if(res.getCount()>0){
                has = true;
            }
            //TODO: check if this car already have pending/ready AD binded
        }finally {
            if(res != null)
                res.close();
        }
        return has;
    }

    public boolean driverOrder(int userId, int adId, String pos){
        //1. found car_id of userId; CREATE TABLE ad_car(id INTEGER PRIMARY KEY autoincrement, position TEXT,status TEXT,car_id INTEGER,ad_id INTEGER);
        Cursor res = null;
        int car_id = -1;
        try{
            res = db.rawQuery("SELECT * FROM "+DBHelper.TABLE_CAR+" WHERE user_id=?", new String[]{String.valueOf(userId)});
            if(res.getCount()>0){
                res.moveToFirst();
                car_id = res.getInt(res.getColumnIndex(DBHelper.CAR_COL_ID));
            }
            //TODO: check if this car already have pending/ready AD binded 
        }finally {
           if(res != null)
               res.close();
        }
        if(car_id != -1){
            //2. create item form ad_car
            db.beginTransactionNonExclusive();
            String query = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES ('%s', '%s', %d, %d);"
                    , DBHelper.TABLE_AD_CAR
                    ,DBHelper.ADCAR_COL_POSITION,DBHelper.ADCAR_COL_STATUS,DBHelper.ADCAR_COL_CARID,DBHelper.ADCAR_COL_ADID
                    ,pos,"pending",car_id,adId);
            db.execSQL(query);
            query = String.format("UPDATE %s SET %s = %s -1 WHERE %s = %d;", DBHelper.TABLE_AD, DBHelper.AD_COL_CARS_NOW, DBHelper.AD_COL_CARS_NOW
            ,DBHelper.AD_COL_ID, adId);
            db.execSQL(query);
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        }

        return false;
    }

    public boolean driverCancelOrder(int userId, int adId){
        //1. found car_id of userId; CREATE TABLE ad_car(id INTEGER PRIMARY KEY autoincrement, position TEXT,status TEXT,car_id INTEGER,ad_id INTEGER);
        Cursor res = null;
        int car_id = -1;
        try{
            res = db.rawQuery("SELECT * FROM "+DBHelper.TABLE_CAR+" WHERE user_id=?", new String[]{String.valueOf(userId)});
            if(res.getCount()>0){
                res.moveToFirst();
                car_id = res.getInt(res.getColumnIndex(DBHelper.CAR_COL_ID));
            }
            //TODO: check if this car already have pending/ready AD binded
        }finally {
            if(res != null)
                res.close();
        }
        if(car_id != -1){
            //2. create item form ad_car
            db.beginTransactionNonExclusive();
            String query = String.format("DELETE FROM %s WHERE %s=%d;"
                    , DBHelper.TABLE_AD_CAR, DBHelper.ADCAR_COL_CARID
                    , car_id);
            db.execSQL(query);
            query = String.format("UPDATE %s SET %s = %s + 1 WHERE %s = %d;", DBHelper.TABLE_AD, DBHelper.AD_COL_CARS_NOW, DBHelper.AD_COL_CARS_NOW
                    ,DBHelper.AD_COL_ID, adId);
            db.execSQL(query);
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        }
        return false;
    }

    public boolean vendorInstall(int carId, int adId){
        //ad_id not used so far, car can only install it requested AD
        //1. update the status of ad_car
         db.execSQL(String.format("UPDATE %s SET %s='%s' WHERE car_id=%d;"
                , DBHelper.TABLE_AD_CAR
                ,DBHelper.ADCAR_COL_STATUS
                ,"ready", carId));       
        //2. verify
        return true;
    }

    public boolean vendorUninstall(int carId, int adId){
        //1. update the status of ad_car
        //TODO: check if date finished
         db.execSQL(String.format("UPDATE %s SET %s='%s' WHERE car_id=%d;"
                , DBHelper.TABLE_AD_CAR
                ,DBHelper.ADCAR_COL_STATUS
                ,"finished", carId));           
        //2. verify
        return true;
    }

    public boolean userLogin(String name, String passwd, int role){
        Cursor res = null;
        boolean found = false;
        String strrole = (role== UserState.USER_ROLE_CLIENT)?"client":"driver";
        try{
            res = db.query(DBHelper.TABLE_USER, null, "name=? AND passwd=? AND type=?", new String[]{name,passwd, strrole}, null, null, null);
            if(res.getCount()>0){
                found = true;
            }
        }finally {
            res.close();
        }        
        return found;
    }

    public boolean userCreate(String role, String name, String passwd){
        return true;
    }


    private DemoAD cursorToAD(Cursor res){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        DemoAD ad = new DemoAD();
        ad.setId(res.getLong(res.getColumnIndex(DBHelper.AD_COL_ID)));
        ad.setCompany(res.getString(res.getColumnIndex(DBHelper.AD_COL_COMPANY)));
        ad.setCars(res.getInt(res.getColumnIndex(DBHelper.AD_COL_CARS)));
        ad.setStartDate(dateFormatter.format(res.getLong(res.getColumnIndex(DBHelper.AD_COL_START_DATE))));
        ad.setEndDate(dateFormatter.format(res.getLong(res.getColumnIndex(DBHelper.AD_COL_END_DATE))));
        ad.setLogo(res.getString(res.getColumnIndex(DBHelper.AD_COL_LOGO_URI)));
        ad.setModel(res.getString(res.getColumnIndex(DBHelper.AD_COL_MODEL_URL)));
        ad.setUserId(res.getLong(res.getColumnIndex(DBHelper.AD_COL_USER_ID)));
        ad.setCarsNow(res.getInt(res.getColumnIndex(DBHelper.AD_COL_CARS_NOW)));
        return ad;
    }

    public String fetchOptionValue(String key){
        Cursor res = null;
        String value = null;
        try {
            res = db.query(DBHelper.TABLE_OPTION, null, "key=?", new String[]{key}, null, null, null);
            if(res.getCount() == 1){
                res.moveToFirst();
                value = res.getString(res.getColumnIndex(DBHelper.OPTION_COL_VALUE));
            }
        }finally {
            res.close();
        }
        return value;
    }

    public void saveOptionValue(String key, String value){
        Cursor res = null;
        try{
            res = db.query(DBHelper.TABLE_OPTION, null, "key=?", new String[]{key}, null, null, null);
            if(res.getCount()>0){
                //update
                ContentValues values = new ContentValues();
                values.put(DBHelper.OPTION_COL_VALUE, value);
                db.update(DBHelper.TABLE_OPTION,
                        values,
                        DBHelper.OPTION_COL_KEY + " = ?",
                        new String[]{key});
            }else{
                //create
                ContentValues values = new ContentValues();
                values.put(DBHelper.OPTION_COL_KEY, key);
                values.put(DBHelper.OPTION_COL_VALUE, value);
                db.insert(DBHelper.TABLE_OPTION, null, values);
            }

        }finally {
            res.close();
        }

    }

    public void resetOption() {
        db.execSQL(String.format("DELETE FROM %s ;"
                , DBHelper.TABLE_OPTION));
    }

    public int getUserIdByName(String name){
        Cursor res = null;
        int user_id = -1;
        try{
            res = db.query(DBHelper.TABLE_USER, null, "name=?", new String[]{name}, null, null, null);
            if(res.getCount() > 0){
                res.moveToFirst();
                user_id = res.getInt(res.getColumnIndex(DBHelper.USER_COL_ID));
            }
        }finally {
            res.close();
        }
        return user_id;
    }

    public long addUserCert(DemoUserCert userCert){
        ContentValues cv = new  ContentValues();
        cv.put(DBHelper.USER_CERT_COL_NAME,    userCert.getName());
        cv.put(DBHelper.USER_CERT_COL_IDENTITY,    userCert.getIdentity());
        //cv.put(DBHelper.USER_CERT_COL_PHOTO,    userCert.getPhoto());
        cv.put(DBHelper.USER_CERT_COL_PHOTO,    new byte[0]);//don't save bitmap, it eat too much memory
        cv.put(DBHelper.USER_CERT_COL_CITY_ID,    getCityIdByName(userCert.getCity()));
        cv.put(DBHelper.USER_CERT_COL_USER_ID,    userCert.getUserId());
        return db.insert(DBHelper.TABLE_USER_CERTIFICATE, null, cv);
    }

    public void unbindUserCert(String username){
        db.execSQL(String.format("DELETE FROM %s WHERE %s in (SELECT id FROM %s WHERE name='%s');"
                , DBHelper.TABLE_USER_CERTIFICATE, DBHelper.USER_CERT_COL_USER_ID, DBHelper.TABLE_USER, username));
    }

    public DemoUserCert getUserCertByUserName(String username){
        DemoUserCert userCert = null;
        Cursor res = null;
        if(username == null || username.length() == 0)
            return userCert;
        else
            res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_USER_CERTIFICATE + " WHERE user_id in (SELECT id FROM " + DBHelper.TABLE_USER +" WHERE name='"+username+ "');", null);
        
        if(res.getCount() >  0){
            userCert = new DemoUserCert();
            res.moveToFirst();
            String name = res.getString(res.getColumnIndex(DBHelper.USER_CERT_COL_NAME));
            userCert.setName(res.getString(res.getColumnIndex(DBHelper.USER_CERT_COL_NAME)));
            userCert.setIdentity(res.getString(res.getColumnIndex(DBHelper.USER_CERT_COL_IDENTITY)));
            userCert.setCity(getCityNameById(res.getInt(res.getColumnIndex(DBHelper.USER_CERT_COL_CITY_ID))));
            userCert.setUserId(res.getInt(res.getColumnIndex(DBHelper.USER_CERT_COL_USER_ID)));
            //userCert.setPhoto(res.getBlob(res.getColumnIndex(DBHelper.USER_CERT_COL_PHOTO)));
        }
        res.close();
        return userCert;   
    }

    public String getCityNameById(int cityId){
        Cursor res = null;
        String city = "";
        try{
            res = db.rawQuery("SELECT name FROM "+DBHelper.TABLE_CITY+" WHERE id=?", new String[]{String.valueOf(cityId)});
            if(res.getCount()>0){
                res.moveToFirst();
                city = res.getString(res.getColumnIndex(DBHelper.CITY_COL_NAME));
            }
        }finally {
            res.close();
        }
        return city;
    }

    public int getCityIdByName(String name){
        Cursor res = null;
        int cityid = -1;
        try{
            res = db.rawQuery("SELECT id FROM "+DBHelper.TABLE_CITY+" WHERE name=?", new String[]{name});
            if(res.getCount()>0){
                res.moveToFirst();
                cityid = res.getInt(res.getColumnIndex(DBHelper.CITY_COL_ID));
            }
        }finally {
            res.close();
        }
        return cityid;
    }

    public static final int BIND_TYPE_ADDRESS = 0;
    public static final int BIND_TYPE_PHONE =  1;
    public static final int BIND_TYPE_EMAIL = 2;

    public boolean updateUserInfo(int user_id, int type, String info){
        boolean ret = false;
        String col = "";
        switch (type){
            case BIND_TYPE_ADDRESS:
                col = DBHelper.USER_COL_ADDRESS;
                break;
            case BIND_TYPE_PHONE:
                col = DBHelper.USER_COL_PHONE;
                break;
            case BIND_TYPE_EMAIL:
                col = DBHelper.USER_COL_EMAIL;
                break;
            default:
                return ret;
        }
        db.execSQL(String.format("UPDATE %s SET %s='%s' WHERE id=%d;"
                , DBHelper.TABLE_USER
                , col,info
                , user_id));
        return true;
    }

    public DemoUser getUserByUserName(String username){
        DemoUser user = null;
        Cursor res = null;
        if(username == null || username.length() == 0)
            return user;
        else
            res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_USER + " WHERE name='"+username+ "';", null);

        if(res.getCount() >  0){
            res.moveToFirst();
            user = new DemoUser();
            user.setId(res.getLong(res.getColumnIndex(DBHelper.USER_COL_ID)));
            user.setName(username);
            user.setAddr(res.getString(res.getColumnIndex(DBHelper.USER_COL_ADDRESS)));
            user.setPhone(res.getString(res.getColumnIndex(DBHelper.USER_COL_PHONE)));
            user.setEmail(res.getString(res.getColumnIndex(DBHelper.USER_COL_EMAIL)));
        }
        res.close();
        return user;
    }

    public List<DemoCar> getCarsOfUser(int user_id){
        List<DemoCar> ads = new ArrayList<>();
        Cursor res = null;
        res = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_CAR + " WHERE user_id = " +user_id+ ";", null);
        res.moveToFirst();
        while(!res.isAfterLast())
        {
            DemoCar ad = new DemoCar();
            ad.setId(res.getLong(res.getColumnIndex(DBHelper.CAR_COL_ID)));
            ad.setPlate(res.getString(res.getColumnIndex(DBHelper.CAR_COL_PLATE_NUM)));
            ad.setBrand(res.getString(res.getColumnIndex(DBHelper.CAR_COL_BRAND)));
            ad.setModel(res.getString(res.getColumnIndex(DBHelper.CAR_COL_MODEL)));
            ads.add(ad);
            res.moveToNext();
        }
        res.close();
        return ads;
    }

    public boolean saveCar(DemoCar car){
        boolean ret = true;
        ContentValues cv = new  ContentValues();
        cv.put(DBHelper.CAR_COL_BRAND,    car.getBrand());
        cv.put(DBHelper.CAR_COL_MODEL,    car.getModel());
        cv.put(DBHelper.CAR_COL_PLATE_NUM, car.getPlate());
        cv.put(DBHelper.CAR_COL_USER_ID, car.getUserId());
        if(db.insert(DBHelper.TABLE_CAR, null, cv)> 0)
            return true;
        else
            return false;
    }

    public boolean deleteCarById(int car_id){
        boolean ret = true;
        int num = db.delete(DBHelper.TABLE_CAR, "id=?", new String[]{String.valueOf(car_id)});
        return num==1;
    }
}
