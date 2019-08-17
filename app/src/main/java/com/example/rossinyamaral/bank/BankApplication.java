package com.example.rossinyamaral.bank;

import android.app.Application;

/**
 * Created by rossinyamaral on 08/12/18.
 */

public class BankApplication extends Application {
    public static final String TAG = BankApplication.class.getSimpleName();
    private static BankApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        BankApi.setService(new BankServicesImpl(mInstance.getApplicationContext()));
        BankCache.setStorage(new BankStorageImpl(mInstance.getApplicationContext()));
    }

    public static BankApplication getInstance() {
        return mInstance;
    }


}
