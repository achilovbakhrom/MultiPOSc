package com.jim.multipos.utils.managers;

import android.content.Context;

/**
 * Created by Developer on 5/23/17.
 */

public class CurrencyManager {

    private static CurrencyManager instance;
    private Context context;

    public static CurrencyManager getInstance(Context context) {
        if (instance == null) {
            instance = new CurrencyManager(context);
        }
        return instance;
    }

    private CurrencyManager(Context context) {
        this.context = context;
    }

}
