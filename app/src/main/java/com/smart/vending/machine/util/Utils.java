package com.smart.vending.machine.util;

import com.smart.vending.machine.application.Application;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by MUVVASR on 3/7/2018.
 */

public class Utils {

    public static boolean isValidCoin(String pInsertedCoin) {
        return Application.mApplication.mValidCoins.containsKey(pInsertedCoin);
    }

}
