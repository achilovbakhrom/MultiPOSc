package com.jim.multipos.utils.managers;

import android.content.Context;

/**
 * Created by Developer on 5/23/17.
 */

public class MPModelManager {
    private Context context;
    private static MPModelManager instance;
    public static MPModelManager getInstance(Context context) {
        if (instance == null) {
            instance = new MPModelManager(context);
        }
        return instance;
    }

    private MPModelManager(Context context) {
        this.context = context;
    }
}
