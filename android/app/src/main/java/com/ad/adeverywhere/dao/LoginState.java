package com.ad.adeverywhere.dao;

/**
 * Created by Xiaosen_Wang on 9/9/2016.
 */
public class LoginState {
    private static LoginState ourInstance = new LoginState();

    public static LoginState getInstance() {
        return ourInstance;
    }

    private LoginState() {
    }
}
