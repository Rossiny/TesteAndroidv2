package com.example.rossinyamaral.bank;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class BankStorageImpl implements BankStorage {

    private Context context;

    BankStorageImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getLastLogin() {
        return getParam("lastLogin");
    }

    @Override
    public void setLastLogin(String username/*, String password*/) {
        setParam("lastLogin", username);
//        setParam("lastPassword", password);
    }

    private String getParam(String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "sharedParam", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    @SuppressLint("ApplySharedPref")
    private void setParam(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "sharedParam", Context.MODE_PRIVATE);
        sharedPref.edit().putString(key, value).commit();
    }

    public String getLastPassword() {
        return getParam("lastPassword");
    }
}
