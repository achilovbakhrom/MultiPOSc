package com.jim.mpviews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by developer on 21.05.2017.
 */

public class StateSaver {
    private SharedPreferences sharedPreferences;
    private static StateSaver instance;
    public static StateSaver getInstance(Context context){
        if(instance==null){
            instance = new StateSaver(context);
        }
        return instance;
    }
    private StateSaver(Context context){
        sharedPreferences = context.getSharedPreferences("mpviews",Context.MODE_PRIVATE);
    }

    public SharedPreferences getStateSaver(){
        return sharedPreferences;
    }
}
