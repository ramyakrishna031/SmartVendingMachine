package com.smart.vending.machine.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.smart.vending.machine.util.Utils;

import java.util.HashMap;

/**
 * Created by MUVVASR on 3/7/2018.
 */

public class Application extends android.app.Application {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String mSharedPreferenceName = "vending_machine";
    public static Application mApplication;
    private static final String PRODUCT1 = "Cola ($1.00)";
    private static final String PRODUCT2 = "Chips ($0.50)";
    private static final String PRODUCT3 = "Candy ($0.65)";


    private static final float PRODUCT1_PRICE = 1.00f;
    private static final float PRODUCT2_PRICE = 0.50f;
    private static final float PRODUCT3_PRICE = 0.65f;

    public HashMap<String, Float> mProductPriceMap ;
    public HashMap<String, Integer> mProductCountMap ;
    public HashMap<String,Float> mValidCoins;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mSharedPreferences = this.getSharedPreferences(mSharedPreferenceName, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mProductPriceMap = new HashMap<>();
        mProductCountMap = new HashMap<>();
        mValidCoins =new HashMap<String,Float>();
        initializeValidCoinsSet();
        initializeProductPriceMap();
        rechargeProducts();
    }

    private void rechargeProducts() {
        mProductCountMap.put(PRODUCT1,5);
        mProductCountMap.put(PRODUCT2,5);
        mProductCountMap.put(PRODUCT3,5);
    }

    private void initializeValidCoinsSet() {
        mValidCoins.put("nickels", 0.05f);
        mValidCoins.put("dimes", 0.1f);
        mValidCoins.put("quarters", 0.25f);
    }

    public void initializeProductPriceMap() {
        mProductPriceMap.put(PRODUCT1,PRODUCT1_PRICE);
        mProductPriceMap.put(PRODUCT2,PRODUCT2_PRICE);
        mProductPriceMap.put(PRODUCT3,PRODUCT3_PRICE);
    }


    public void saveSharedPreferenceFloatType(String pKey, float pValue) {
        mEditor.putFloat(pKey, pValue);
        mEditor.commit();
    }

    public float getSharedPreferenceFloatValue(String pKey) {
        return mSharedPreferences.getFloat(pKey, 0);
    }
}
