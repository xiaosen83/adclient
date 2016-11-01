package com.ad.adeverywhere.demo;

import android.content.Context;

import com.ad.adeverywhere.dao.UserState;

/**
 * Created by cwang on 10/13/16.
 */

public class DemoPersistData {
    private static DemoPersistData ourInstance = new DemoPersistData();
    public static DemoPersistData getInstance() {
        return ourInstance;
    }
    private DemoPersistData() {}

    private String username = "";
    private String role = "";
    private boolean authed = false;
    private Context context = null;

    public void setContext(Context ctx){
        context = ctx;
    }
    public boolean isAuthed() {
        return authed;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthed(boolean authed) {
        this.authed = authed;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void loadData(){
        DemoDataSource ds = new DemoDataSource(context);
        ds.open();
        String role = ds.fetchOptionValue(DBHelper.OPTION_KEY_ROLE);
        String auth = ds.fetchOptionValue(DBHelper.OPTION_KEY_AUTHED);
        String username = ds.fetchOptionValue(DBHelper.OPTION_KEY_USERNAME);

        setUsername(ds.fetchOptionValue(DBHelper.OPTION_KEY_USERNAME));
        setRole(ds.fetchOptionValue(DBHelper.OPTION_KEY_ROLE));
        String ss = ds.fetchOptionValue(DBHelper.OPTION_KEY_AUTHED);
        setAuthed((ss!=null && ss.equalsIgnoreCase("true"))? true:false);
        if(isAuthed())
            UserState.getInstance().setUser(ds.getUserByUserName(getUsername()));
        ds.close();
        UserState.getInstance().setAuthed(authed);
        UserState.getInstance().setUsername(username);
        UserState.getInstance().setRole(Integer.parseInt(role==null?String.valueOf(UserState.USER_ROLE_DRIVER):role));
    }

    public void saveData(){
        DemoDataSource ds = new DemoDataSource(context);
        ds.open();
        String role = String.valueOf(UserState.getInstance().getRole());
        String auth = String.valueOf(UserState.getInstance().isAuthed());
        String username = UserState.getInstance().getUsername();
        ds.saveOptionValue(DBHelper.OPTION_KEY_USERNAME, UserState.getInstance().getUsername());
        ds.saveOptionValue(DBHelper.OPTION_KEY_ROLE, String.valueOf(UserState.getInstance().getRole()));
        ds.saveOptionValue(DBHelper.OPTION_KEY_AUTHED, String.valueOf(UserState.getInstance().isAuthed()));
        ds.close();
    }

    public void resetData(){
        DemoDataSource ds = new DemoDataSource(context);
        ds.open();
        ds.resetOption();
        ds.close();
    }
}
