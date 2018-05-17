package com.jim.multipos.utils.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.jim.multipos.utils.AppConstants;

import java.util.Locale;

public class LocaleManger {

    public static final String ENGLISH = "en";
    public static final String RUSSIAN = "ru";
    private static final String LANGUAGE_CODE = "LANGUAGE_CODE";

    public static Context setLocale(Context context) {
        return updateResources(context, getLanguage(context));
    }

    public static Context setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        return updateResources(c, language);
    }

    private static void persistLanguage(Context context, String language) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREFERENCE_APPLEVEL, Context.MODE_PRIVATE);
        prefs.edit().putString(LANGUAGE_CODE, language).commit();
    }

    private static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREFERENCE_APPLEVEL, Context.MODE_PRIVATE);
        return prefs.getString(LANGUAGE_CODE, ENGLISH);
    }

    private static Context updateResources(Context context, String language) {
        Locale myLocale = new Locale(language);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return config.locale;
    }
}
