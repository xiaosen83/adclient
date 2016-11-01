package com.ad.adeverywhere.dao;

import android.content.Context;

import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.demo.DemoPersistData;
import com.ad.adeverywhere.demo.DemoUser;
import com.ad.adeverywhere.demo.DemoUserCert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xiaosen_Wang on 9/9/2016.
 */
public class UserState {
    private static UserState ourInstance = new UserState();

    public static final int  USER_ROLE_UNKNOWN = 0;
    public static final int  USER_ROLE_DRIVER = 1;
    public static final int USER_ROLE_CLIENT = 2;
    public static final int USER_ROLE_VENDOR = 3;

    public static final int  AD_STATE_INUSE = 0;
    public static final int  AD_STATE_PENDING = 1;
    public static final int  AD_STATE_FINISHED = 2;

    private  int  mRole = USER_ROLE_DRIVER;
    private  boolean mAuthed = false;
    private String username="";
    private int userId = -1;

    //bind info

    private DemoUserCert userCert = null;
    private DemoUser user = null;

    private List<Ad> mDriverCurrentAds = new ArrayList<Ad>();
    private List<Ad> mDriverPendingAds = new ArrayList<Ad>();
    private List<Ad> mDriverHistoryAds = new ArrayList<Ad>();

    private Map<Ad, Integer> mClientAds = new HashMap<Ad, Integer>();

    public static UserState getInstance() {
        return ourInstance;
    }

    private UserState() {
    }

    public void didLogout(){
        mAuthed = false;
        userCert = null;
        user = null;
        DemoPersistData.getInstance().saveData();
    }

    public DemoUser getUser() {
        return user;
    }
    public void setUser(DemoUser user){
        this.user = user;
    }

    public void setUserCert(DemoUserCert userCert){
        this.userCert = userCert;
    }

    public DemoUserCert getUserCert(Context context, Boolean justCheck) {
        if(userCert != null || justCheck)
            return userCert;
        else if(username != null && username.length()>0  && isAuthed()){
            DemoDataSource ds = new DemoDataSource(context);
            ds.open();
            userCert = ds.getUserCertByUserName(username);
            ds.close();
        }
        return userCert;
    }

    public int getUserId() {
        return userId;
    }

    public int getUserId(Context context){
        if(userId != -1)
            return userId;
        else if(context != null){
            DemoDataSource ds = new DemoDataSource(context);
            ds.open();
            setUserId(ds.getUserIdByName(username));
            ds.close();
        }
        return userId;
    }

    public void setUserId(int userid){
        userId = userid;
    }

    public int  getRole(){return mRole;}
    public void setRole(int  role){
        mRole = role;
    }

    public void setRole(int  role, boolean save){
        setRole(role);
        if(save) {
            DemoPersistData.getInstance().setRole(String.valueOf(role));
            DemoPersistData.getInstance().saveData();
        }
    }

    public boolean isAuthed(){return mAuthed;}
    public void  setAuthed(boolean authed){
        mAuthed = authed;
        if(!authed)
            userId = -1;
    }
    public void  setAuthed(boolean authed, boolean save){
        setAuthed(authed);
        if(save) {
            DemoPersistData.getInstance().setAuthed(authed);
            DemoPersistData.getInstance().saveData();
        }
    }
    public String getUsername(){return username;}
    public void setUsername(String username){
        this.username = username;
    }
    public void setUsername(String username, boolean save){
        setUsername(username);
        if(save) {
            DemoPersistData.getInstance().setUsername(username);
            DemoPersistData.getInstance().saveData();
        }
    }

    public List<Ad> getDriverCurrentAds(){
        return  mDriverCurrentAds;
    }
    public List<Ad> getDriverPendingAds(){
        return  mDriverPendingAds;
    }
    public List<Ad> getDriverHistoryAds(){
        return  mDriverHistoryAds;
    }
    public Map<Ad, Integer> getClientAds(){
        return  mClientAds;
    }
    public void loadDriverAds(){
        //TODO
    }
    public void loadClientAds(){
        //TODO
    }

    public void loadDriverAdsDummy(){
        mDriverCurrentAds.clear();
        mDriverPendingAds.clear();
        mDriverHistoryAds.clear();

        Ad adCurrent = new Ad();
        adCurrent.setCompany("Company current");
        adCurrent.setStartDate("2016-9-1");
        adCurrent.setEndDate("2016-11-1");
        adCurrent.setLogoUri("http://download.easyicon.net/png/511332/48/");;
        adCurrent.setAdUri("http://d5.sina.com.cn/pfpghc2/201609/18/87e9a10c82c9469a88d89ff4249c1f10.jpg");
        mDriverCurrentAds.add(adCurrent);

        adCurrent = new Ad();
        adCurrent.setCompany("Company pending");
        adCurrent.setStartDate("2016-9-1");
        adCurrent.setEndDate("2016-11-1");
        adCurrent.setLogoUri("http://download.easyicon.net/png/511332/48/");;
        adCurrent.setAdUri("http://d1.leju.com/ia/2016/09/13/f57d7bc34859baimg.jpg");
        mDriverPendingAds.add(adCurrent);

        adCurrent = new Ad();
        adCurrent.setCompany("Company history 1");
        adCurrent.setStartDate("2016-9-1");
        adCurrent.setEndDate("2016-11-1");
        adCurrent.setLogoUri("http://download.easyicon.net/png/511332/48/");;
        adCurrent.setAdUri("https://img13.360buyimg.com/da/jfs/t3211/231/2392389384/26372/4378e8df/57e09c81N8f2af494.jpg");
        mDriverHistoryAds.add(adCurrent);

        adCurrent = new Ad();
        adCurrent.setCompany("Company history 2");
        adCurrent.setStartDate("2016-9-1");
        adCurrent.setEndDate("2016-11-1");
        adCurrent.setLogoUri("http://download.easyicon.net/png/511332/48/");;
        adCurrent.setAdUri("http://www.jdxc.sh.cn/data/attachment/portal/201510/27/165414bjo0g7zqdoaoga5a.jpg");
        mDriverHistoryAds.add(adCurrent);
    }

}
