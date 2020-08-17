package com.gogrocersm.storemanager.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.gogrocersm.storemanager.Dashboard.LoginActivity;

import java.util.HashMap;

import static com.gogrocersm.storemanager.Config.BaseURL.IS_LOGIN;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_DATE;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_EMAIL;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_HOUSE;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_ID;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_IMAGE;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_MOBILE;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_NAME;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_PASSWORD;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_PINCODE;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_SOCITY_ID;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_SOCITY_NAME;
import static com.gogrocersm.storemanager.Config.BaseURL.KEY_TIME;
import static com.gogrocersm.storemanager.Config.BaseURL.PREFS_NAME;
import static com.gogrocersm.storemanager.Config.BaseURL.PREFS_NAME2;
import static com.gogrocersm.storemanager.Config.BaseURL.USER_CURRENCY;
import static com.gogrocersm.storemanager.Config.BaseURL.USER_CURRENCY_CNTRY;

/**
 * Created by Rajesh Dabhi on 28/6/2017.
 */

public class Session_management {

    SharedPreferences prefs;

    SharedPreferences prefs2;

    SharedPreferences.Editor editor;

    SharedPreferences.Editor editor2;

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences(PREFS_NAME2, PRIVATE_MODE);
        editor2 = prefs2.edit();
    }

    public void createLoginSession(String id, String email, String name
            , String mobile, String pincode, String socity_id,
                                   String socity_name, String house, String password) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_HOUSE, house);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, prefs.getString(KEY_ID, ""));
        // user email id
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, ""));
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, ""));

        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, ""));

        user.put(KEY_IMAGE, prefs.getString(KEY_IMAGE, ""));

        user.put(KEY_PINCODE, prefs.getString(KEY_PINCODE, ""));

        user.put(KEY_SOCITY_ID, prefs.getString(KEY_SOCITY_ID, ""));

        user.put(KEY_SOCITY_NAME, prefs.getString(KEY_SOCITY_NAME, ""));

        user.put(KEY_HOUSE, prefs.getString(KEY_HOUSE, ""));

        user.put(KEY_PASSWORD, prefs.getString(KEY_PASSWORD, ""));

        // return user
        return user;
    }

    public void updateData(String name, String mobile, String pincode
            , String socity_id, String image, String wallet, String rewards, String house) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_IMAGE, image);
//        editor.putString(KEY_WALLET_Ammount, wallet);
//        editor.putString(KEY_REWARDS_POINTS, rewards);
        editor.putString(KEY_HOUSE, house);

        editor.apply();
    }

    public void updateSocity(String socity_name, String socity_id) {
        editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_SOCITY_ID, socity_id);

        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, LoginActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void logoutSessionwithchangepassword() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void creatdatetime(String date, String time) {
        editor2.putString(KEY_DATE, date);
        editor2.putString(KEY_TIME, time);

        editor2.commit();
    }

    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }

    public HashMap<String, String> getdatetime() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_DATE, prefs2.getString(KEY_DATE, null));
        user.put(KEY_TIME, prefs2.getString(KEY_TIME, null));

        return user;
    }

    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

    public String getCurrency() {
        return prefs.getString(USER_CURRENCY, "");
    }

    public String getCurrencyCountry() {
        return prefs.getString(USER_CURRENCY_CNTRY, "");
    }

    public void setCurrency(String name, String currency) {
        prefs.edit().putString(USER_CURRENCY,currency).apply();
        prefs.edit().putString(USER_CURRENCY_CNTRY,name).apply();
    }


}
