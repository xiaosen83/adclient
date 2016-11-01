package com.ad.adeverywhere.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ad.adeverywhere.utils.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

/**
 * Created by cwang on 10/9/16.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBHelper";
    private static final Logger logger = Logger.getInstance();

    public static final int DB_VERSION = 2;

    public static final String DB_NAME = "adcar.db";

    public static final String TABLE_AD = "ad";
    public static final String AD_COL_ID = "id";
    public static final String AD_COL_COMPANY = "company";
    public static final String AD_COL_CARS = "cars";
    public static final String AD_COL_CARS_NOW = "cars_now";
    public static final String AD_COL_START_DATE = "start_date";
    public static final String AD_COL_END_DATE = "end_date";
    public static final String AD_COL_LOGO_URI = "logo";
    public static final String AD_COL_MODEL_URL = "model";
    public static final String AD_COL_USER_ID = "user_id";

    public static final String SQL_CREATE_AD = "CREATE TABLE " + TABLE_AD
            + "("
            + AD_COL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + AD_COL_COMPANY + " TEXT,"
            + AD_COL_CARS + " INTEGER,"
            + AD_COL_CARS_NOW + " INTEGER,"
            + AD_COL_START_DATE + " INTEGER,"
            + AD_COL_END_DATE + " INTEGER,"
            + AD_COL_LOGO_URI + " TEXT,"
            + AD_COL_MODEL_URL + " TEXT,"
            + AD_COL_USER_ID + " INTEGER"
            + ");";
    public static final String SQL_DELETE_AD = "DROP TABLE IF EXISTS " + TABLE_AD;

    public static final String TABLE_USER = "user";
    public static final String USER_COL_ID = "id";
    public static final String USER_COL_NAME = "name";
    public static final String USER_COL_TYPE = "type";
    public static final String USER_COL_PASSWD = "passwd";
    public static final String USER_COL_ADDRESS = "address";
    public static final String USER_COL_PHONE = "phone";
    public static final String USER_COL_EMAIL = "email";

    public static final String SQL_CREATE_USER = "CREATE TABLE " + TABLE_USER
            + "("
            + USER_COL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + USER_COL_NAME + " TEXT,"
            + USER_COL_TYPE + " TEXT,"
            + USER_COL_PASSWD + " TEXT,"
            + USER_COL_ADDRESS + " TEXT,"
            + USER_COL_PHONE + " TEXT,"
            + USER_COL_EMAIL + " TEXT"
            + ");";
    public static final String SQL_DELETE_USER = "DROP TABLE IF EXISTS " + TABLE_USER;

    public static final String TABLE_USER_CERTIFICATE = "user_cert";
    public static final String USER_CERT_COL_ID = "id";
    public static final String USER_CERT_COL_IDENTITY = "identity";
    public static final String USER_CERT_COL_NAME = "name"; //real name
    public static final String USER_CERT_COL_PHOTO = "photo"; //blob type
    public static final String USER_CERT_COL_CITY_ID = "city_id";
    public static final String USER_CERT_COL_USER_ID = "user_id";

    public static final String SQL_CREATE_USER_CERT = "CREATE TABLE " + TABLE_USER_CERTIFICATE
            + "("
            + USER_CERT_COL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + USER_CERT_COL_IDENTITY + " TEXT,"
            + USER_CERT_COL_NAME + " TEXT,"
            + USER_CERT_COL_PHOTO + " BLOB,"
            + USER_CERT_COL_CITY_ID + " INTEGER,"
            + USER_CERT_COL_USER_ID + " INTEGER"
            + ");";
    public static final String SQL_DELETE_USER_CERT = "DROP TABLE IF EXISTS " + TABLE_USER_CERTIFICATE;

    public static final String TABLE_USER_CONTACT = "user_contact";
    public static final String USER_CONTACT_COL_ID = "id";
    public static final String USER_CONTACT_COL_ADDRESS = "address";
    public static final String USER_CONTACT_COL_PHONE = "phone";
    public static final String USER_CONTACT_COL_USER_ID = "user_id";

    public static final String SQL_CREATE_USER_CONTACT = "CREATE TABLE " + TABLE_USER_CONTACT
            + "("
            + USER_CONTACT_COL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + USER_CONTACT_COL_ADDRESS + " TEXT,"
            + USER_CONTACT_COL_PHONE + " TEXT,"
            + USER_CONTACT_COL_USER_ID + " INTEGER"
            + ");";
    public static final String SQL_DELETE_USER_CONTACT = "DROP TABLE IF EXISTS " + TABLE_USER_CONTACT;

    public static final String TABLE_CAR = "car";
    public static final String CAR_COL_ID = "id";
    public static final String CAR_COL_PLATE_NUM = "plate_num";
    public static final String CAR_COL_MODEL = "model";
    public static final String CAR_COL_BRAND = "brand";
    public static final String CAR_COL_USER_ID = "user_id";

    public static final String SQL_CREATE_CAR = "CREATE TABLE " + TABLE_CAR
            + "("
            + CAR_COL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + CAR_COL_PLATE_NUM + " TEXT,"
            + CAR_COL_BRAND + " TEXT,"
            + CAR_COL_MODEL + " TEXT,"
            + CAR_COL_USER_ID + " INTEGER"
            + ");";
    public static final String SQL_DELETE_CAR = "DROP TABLE IF EXISTS " + TABLE_CAR;

    public static final String TABLE_AD_CAR = "ad_car";
    public static final String ADCAR_COL_ID = "id";
    public static final String ADCAR_COL_POSITION = "position"; //left, right, tail
    public static final String ADCAR_COL_STATUS = "status"; //pending, ready, finished
    public static final String ADCAR_COL_CARID = "car_id";
    public static final String ADCAR_COL_ADID = "ad_id";

    public static final String SQL_CREATE_AD_CAR = "CREATE TABLE " + TABLE_AD_CAR
            + "("
            + ADCAR_COL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + ADCAR_COL_POSITION + " TEXT,"
            + ADCAR_COL_STATUS + " TEXT,"
            + ADCAR_COL_CARID + " INTEGER,"
            + ADCAR_COL_ADID + " INTEGER"
            + ");";
    public static final String SQL_DELETE_AD_CAR = "DROP TABLE IF EXISTS " + TABLE_AD_CAR;

    public static final String TABLE_CITY = "city";
    public static final String CITY_COL_ID = "id";
    public static final String CITY_COL_CODE = "code";
    public static final String CITY_COL_NAME = "name";

    public static final String SQL_CREATE_CITY = "CREATE TABLE " + TABLE_CITY
            + "("
            + CITY_COL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + CITY_COL_CODE + " TEXT,"
            + CITY_COL_NAME + " TEXT"
            + ");";
    public static final String SQL_DELETE_CITY = "DROP TABLE IF EXISTS " + TABLE_CITY;

    public static final String TABLE_VENDOR = "vendor";
    public static final String VENDOR_COL_ID = "id";
    public static final String VENDOR_COL_TYPE = "type"; //4s? car wash? personal?
    public static final String VENDOR_COL_ADDR = "addr";
    public static final String VENDOR_COL_CITY_ID = "city_id";
    public static final String VENDOR_COL_USER_ID = "user_id";

    public static final String SQL_CREATE_VENDOR = "CREATE TABLE " + TABLE_VENDOR
            + "("
            + VENDOR_COL_ID + " INTEGER PRIMARY KEY autoincrement, "
            + VENDOR_COL_TYPE + " TEXT,"
            + VENDOR_COL_ADDR + " TEXT,"
            + VENDOR_COL_CITY_ID + " INTEGER,"
            + VENDOR_COL_USER_ID + " INTEGER"
            + ");";
    public static final String SQL_DELETE_VENDOR = "DROP TABLE IF EXISTS " + TABLE_VENDOR;

    public static final String TABLE_OPTION = "option";
    public static final String OPTION_COL_KEY = "key";
    public static final String OPTION_COL_VALUE = "value";

    public static final String OPTION_KEY_USERNAME = "username";
    public static final String OPTION_KEY_AUTHED = "authenticated";
    public static final String OPTION_KEY_ROLE = "role";
    public static final String SQL_CREATE_OPTION = "CREATE TABLE " + TABLE_OPTION
            + "("
            + OPTION_COL_KEY + " TEXT PRIMARY KEY, "
            + OPTION_COL_VALUE + " TEXT"
            + ");";
    public static final String SQL_DELETE_OPTION = "DROP TABLE IF EXISTS " + TABLE_OPTION;


    public static final int AD_TYPE_PENDING = 0;
    public static final int AD_TYPE_READY = 1;
    public static final int AD_TYPE_FINISHED = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        logger.logDebug(TAG,SQL_CREATE_AD);
        logger.logDebug(TAG,SQL_CREATE_USER);
        logger.logDebug(TAG,SQL_CREATE_CAR);
        logger.logDebug(TAG,SQL_CREATE_AD_CAR);
        logger.logDebug(TAG,SQL_CREATE_CITY);
        logger.logDebug(TAG,SQL_CREATE_VENDOR);
        db.execSQL(SQL_CREATE_AD);
        db.execSQL(SQL_CREATE_USER);
        db.execSQL(SQL_CREATE_USER_CERT);
        db.execSQL(SQL_CREATE_USER_CONTACT);
        db.execSQL(SQL_CREATE_CAR);
        db.execSQL(SQL_CREATE_AD_CAR);
        db.execSQL(SQL_CREATE_CITY);
        db.execSQL(SQL_CREATE_VENDOR);
        db.execSQL(SQL_CREATE_OPTION);

        loadDummyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_AD);
        db.execSQL(SQL_DELETE_USER);
        db.execSQL(SQL_DELETE_USER_CERT);
        db.execSQL(SQL_DELETE_USER_CONTACT);
        db.execSQL(SQL_DELETE_AD_CAR);
        db.execSQL(SQL_DELETE_CAR);
        db.execSQL(SQL_DELETE_CITY);
        db.execSQL(SQL_DELETE_VENDOR);
        db.execSQL(SQL_DELETE_OPTION);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_AD);
        db.execSQL(SQL_DELETE_USER);
        db.execSQL(SQL_DELETE_USER_CERT);
        db.execSQL(SQL_DELETE_USER_CONTACT);
        db.execSQL(SQL_DELETE_AD_CAR);
        db.execSQL(SQL_DELETE_CAR);
        db.execSQL(SQL_DELETE_CITY);
        db.execSQL(SQL_DELETE_VENDOR);
        db.execSQL(SQL_DELETE_OPTION);
        onCreate(db);
    }

    public void createUser(DemoUser user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COL_NAME, user.getName());
        values.put(USER_COL_PASSWD, user.getPasswd());
        values.put(USER_COL_TYPE, user.getType());
        db.insert(TABLE_USER, null, values);
    }

    private void resetDummyAdCars(SQLiteDatabase db){
        db.execSQL(String.format("DELETE FROM %s;", TABLE_AD_CAR));

        int ad_id = 1, car_id=1;
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', %d, %d);"
                , TABLE_AD_CAR
                ,ADCAR_COL_ID, ADCAR_COL_POSITION,ADCAR_COL_STATUS,ADCAR_COL_CARID,ADCAR_COL_ADID
                ,1,"tail","ready",car_id,ad_id));
        db.execSQL(String.format("UPDATE %s SET %s=%s+1 WHERE %s=%d;", TABLE_AD, AD_COL_CARS_NOW, AD_COL_CARS_NOW, AD_COL_ID, ad_id));

        ad_id = 1; car_id=2;
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', %d, %d);"
                , TABLE_AD_CAR
                ,ADCAR_COL_ID, ADCAR_COL_POSITION,ADCAR_COL_STATUS,ADCAR_COL_CARID,ADCAR_COL_ADID
                ,2,"tail","ready",car_id,ad_id));
        db.execSQL(String.format("UPDATE %s SET %s=%s+1 WHERE %s=%d;", TABLE_AD, AD_COL_CARS_NOW, AD_COL_CARS_NOW, AD_COL_ID, ad_id));
        
    }

    private void resetDummyVendor(SQLiteDatabase db){
        db.execSQL(String.format("DELETE FROM %s;", TABLE_VENDOR));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', %d, %d);"
                , TABLE_VENDOR
                ,VENDOR_COL_ID, VENDOR_COL_TYPE,VENDOR_COL_ADDR,VENDOR_COL_CITY_ID,VENDOR_COL_USER_ID
                ,1,"4s","五角场奔驰4s店",1,7));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', %d, %d);"
                , TABLE_VENDOR
                ,VENDOR_COL_ID, VENDOR_COL_TYPE,VENDOR_COL_ADDR,VENDOR_COL_CITY_ID,VENDOR_COL_USER_ID
                ,2,"4s","四平路大众汽车4s店",1,8));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', %d, %d);"
                , TABLE_VENDOR
                ,VENDOR_COL_ID, VENDOR_COL_TYPE,VENDOR_COL_ADDR,VENDOR_COL_CITY_ID,VENDOR_COL_USER_ID
                ,3,"4s","宝山区比亚迪汽车4s店",1,9));
    }

    private void resetDummyCity(SQLiteDatabase db){
        db.execSQL(String.format("DELETE FROM %s;", TABLE_CITY));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s) VALUES (%d, '%s', '%s');"
                , TABLE_CITY
                ,CITY_COL_ID, CITY_COL_CODE,CITY_COL_NAME
                ,1,"021","上海"));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s) VALUES (%d, '%s', '%s');"
                , TABLE_CITY
                ,CITY_COL_ID, CITY_COL_CODE,CITY_COL_NAME
                ,2,"010","北京"));
    }


    private void resetDummyCar(SQLiteDatabase db){
        db.execSQL(String.format("DELETE FROM %s;", TABLE_CAR));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s', %d);"
                , TABLE_CAR
                ,CAR_COL_ID, CAR_COL_PLATE_NUM,CAR_COL_BRAND,CAR_COL_MODEL,CAR_COL_USER_ID
                ,1,"沪A12345","大众","途观",1));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s', %d);"
                , TABLE_CAR
                ,CAR_COL_ID, CAR_COL_PLATE_NUM,CAR_COL_BRAND,CAR_COL_MODEL,CAR_COL_USER_ID
                ,2,"沪B12345","奥迪","奥迪A4L",2));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s', %d);"
                , TABLE_CAR
                ,CAR_COL_ID, CAR_COL_PLATE_NUM,CAR_COL_BRAND,CAR_COL_MODEL,CAR_COL_USER_ID
                ,3,"沪C12345","宝马","宝马7系",3));
    }

    private void resetDummyUsers(SQLiteDatabase db){
        db.execSQL(String.format("DELETE FROM %s;", TABLE_USER));
        //driver
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s');"
                , TABLE_USER
                ,USER_COL_ID, USER_COL_NAME,USER_COL_TYPE,USER_COL_PASSWD
                ,1,"test1@163.com","driver","111111"));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s');"
                , TABLE_USER
                ,USER_COL_ID, USER_COL_NAME,USER_COL_TYPE,USER_COL_PASSWD
                ,2,"test2@163.com","driver","111111"));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s');"
                , TABLE_USER
                ,USER_COL_ID, USER_COL_NAME,USER_COL_TYPE,USER_COL_PASSWD
                ,3,"test3@163.com","driver","111111"));
        //client
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s');"
                , TABLE_USER
                ,USER_COL_ID, USER_COL_NAME,USER_COL_TYPE,USER_COL_PASSWD
                ,4,"test4@163.com","client","111111"));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s');"
                , TABLE_USER
                ,USER_COL_ID, USER_COL_NAME,USER_COL_TYPE,USER_COL_PASSWD
                ,5,"test5@163.com","client","111111"));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s');"
                , TABLE_USER
                ,USER_COL_ID, USER_COL_NAME,USER_COL_TYPE,USER_COL_PASSWD
                ,6,"test6@163.com","client","111111"));
        //vendor
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s');"
                , TABLE_USER
                ,USER_COL_ID, USER_COL_NAME,USER_COL_TYPE,USER_COL_PASSWD
                ,7,"test7@163.com","vendor","111111"));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s');"
                , TABLE_USER
                ,USER_COL_ID, USER_COL_NAME,USER_COL_TYPE,USER_COL_PASSWD
                ,8,"test8@163.com","vendor","111111"));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s');"
                , TABLE_USER
                ,USER_COL_ID, USER_COL_NAME,USER_COL_TYPE,USER_COL_PASSWD
                ,9,"test9@163.com","vendor","111111"));
    }

    private void resetDummyUserCerts(SQLiteDatabase db) {
        db.execSQL(String.format("DELETE FROM %s;", TABLE_USER_CERTIFICATE));
        //driver
       /* db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (%d, '%s', '%s', '%s', %d, %d);"
                , TABLE_USER_CERTIFICATE
                , USER_CERT_COL_ID, USER_CERT_COL_IDENTITY, USER_CERT_COL_NAME, USER_CERT_COL_PHOTO, USER_CERT_COL_CITY_ID,USER_CERT_COL_USER_ID
                , 1, "310105198305120344", "Jim Hu", "certificate1.jpg", 1, 1));*/
    }

    private void resetDummyUserContacts(SQLiteDatabase db) {
        db.execSQL(String.format("DELETE FROM %s;", TABLE_USER_CONTACT));
        //driver
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (%d, '%s', '%s', %d);"
                , TABLE_USER_CONTACT
                , USER_CONTACT_COL_ID, USER_CONTACT_COL_ADDRESS, USER_CONTACT_COL_PHONE, USER_CONTACT_COL_USER_ID
                , 1, "五角场2000号", "13800000000", 4));
    }

    private void resetDummyAd(SQLiteDatabase db){
        Calendar dateNow = Calendar.getInstance();
        Calendar dateA30 = (Calendar) dateNow.clone(); dateA30.add(Calendar.DATE, -30);
        Calendar dateA90 = (Calendar) dateNow.clone(); dateA90.add(Calendar.DATE, -90);
        Calendar dateB30 = (Calendar) dateNow.clone(); dateB30.add(Calendar.DATE, 30);
        Calendar dateB90 = (Calendar) dateNow.clone(); dateB90.add(Calendar.DATE, 90);

        db.execSQL(String.format("DELETE FROM %s;", TABLE_AD));
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES ('%s', %d, %d, %d, %d, '%s', '%s', %d);"
                , TABLE_AD
                ,AD_COL_COMPANY, AD_COL_CARS,AD_COL_CARS_NOW,AD_COL_START_DATE,AD_COL_END_DATE,AD_COL_LOGO_URI,AD_COL_MODEL_URL,AD_COL_USER_ID
                ,"Apple", 300,0, dateA30.getTimeInMillis(),dateB30.getTimeInMillis(),"test_car1.png","test_ad1.jpg",4);
        logger.logDebug(TAG, sql);
        db.execSQL(sql);
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES ('%s', %d, %d, %d, %d, '%s', '%s', %d);"
                , TABLE_AD
                ,AD_COL_COMPANY, AD_COL_CARS,AD_COL_CARS_NOW,AD_COL_START_DATE,AD_COL_END_DATE,AD_COL_LOGO_URI,AD_COL_MODEL_URL,AD_COL_USER_ID
                ,"Microsoft", 400,0, dateB30.getTimeInMillis(), dateB90.getTimeInMillis(),"test_car2.png","test_ad2.jpg",4));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES ('%s', %d, %d, %d, %d, '%s', '%s', %d);"
                , TABLE_AD
                ,AD_COL_COMPANY, AD_COL_CARS,AD_COL_CARS_NOW,AD_COL_START_DATE,AD_COL_END_DATE,AD_COL_LOGO_URI,AD_COL_MODEL_URL,AD_COL_USER_ID
                ,"Amazon", 500,0, dateA90.getTimeInMillis(), dateA30.getTimeInMillis(),"test_car3.png","test_ad3.jpg",5));
        db.execSQL(String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES ('%s', %d, %d, %d, %d, '%s', '%s', %d);"
                , TABLE_AD
                ,AD_COL_COMPANY, AD_COL_CARS,AD_COL_CARS_NOW,AD_COL_START_DATE,AD_COL_END_DATE,AD_COL_LOGO_URI,AD_COL_MODEL_URL,AD_COL_USER_ID
                ,"Dell", 3000,0, dateA30.getTimeInMillis(), dateB90.getTimeInMillis(),"test_car4png","test_ad4.jpg",6));

    }
    private void loadDummyData(SQLiteDatabase db){
        resetDummyAd(db);
        resetDummyVendor(db);
        resetDummyCity(db);
        resetDummyCar(db);
        resetDummyAdCars(db);
        resetDummyUsers(db);
        resetDummyUserCerts(db);
        resetDummyUserContacts(db);
    }
}
