/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.jim.multipos.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.jim.multipos.utils.AppConstants;

/**
 * Created by janisharali on 27/01/17.
 */

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN";
    private static final String LAST_CATEGORY = "LAST_CATEGORY";
    private static final String LAST_SUBCATEGORY = "LAST_SUBCATEGORY";
    private static final String LAST_PRODUCT = "LAST_PRODUCT";
    private static final String POS_NAME = "posname";
    private static final String EXPERED_TIME = "expired";
    private static final String POS_DETAIL_POS_ID = "POS_DETAIL_POS_ID";
    private static final String POS_DETAIL_ALIAS = "POS_DETAIL_ALIAS";
    private static final String POS_DETAIL_ADDRESS = "POS_DETAIL_ADDRESS";
    private static final String POS_DETAIL_PASSWORD = "POS_DETAIL_PASSWORD";
    private static final String FIRST_CONFIGURED = "FIRST_CONFIGURED";
    private static final String ACTIVE_ITEM_VISIBILITY = "ACTIVE_ITEM_VISIBILITY";

    private final SharedPreferences mPrefs;

    public AppPreferencesHelper(Context context) {
        mPrefs = context.getSharedPreferences(AppConstants.PREFERENCE_APPLEVEL, Context.MODE_PRIVATE);
    }


    @Override
    public long getExperedTime() {
        return mPrefs.getLong(EXPERED_TIME,0);
    }

    @Override
    public void setExperedTime(long experedTime) {
        mPrefs.edit().putLong(EXPERED_TIME,experedTime).apply();
    }

    @Override
    public String getReshreshToken() {
        return mPrefs.getString(PREF_KEY_REFRESH_TOKEN,"");
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        mPrefs.edit().putString(PREF_KEY_REFRESH_TOKEN,refreshToken).apply();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public int getLastPositionCategory() {
        return mPrefs.getInt(LAST_CATEGORY,0);
    }

    @Override
    public void setLastPositionCategory(int position) {
        mPrefs.edit().putInt(LAST_CATEGORY,position).apply();
    }

    @Override
    public int getLastPositionSubCategory(String key) {
        return mPrefs.getInt(LAST_SUBCATEGORY + key, 0);
    }

    @Override
    public void setLastPositionSubCategory(String key, int position) {
        mPrefs.edit().putInt(LAST_SUBCATEGORY + key, position).apply();
    }

    @Override
    public int getLastPositionProduct() {
        return mPrefs.getInt(LAST_PRODUCT, 0);
    }

    @Override
    public void setLastPositionProduct(int position) {
        mPrefs.edit().putInt(LAST_PRODUCT, position).apply();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public void setPosDetailPosId(String posId) {
        mPrefs.edit().putString(POS_DETAIL_POS_ID, posId).apply();
    }

    @Override
    public void setPosDetailAlias(String alias) {
        mPrefs.edit().putString(POS_DETAIL_ALIAS, alias).apply();
    }

    @Override
    public void setPosDetailAddress(String address) {
        mPrefs.edit().putString(POS_DETAIL_ADDRESS, address).apply();
    }

    @Override
    public void setPosDetailPassword(String password) {
        mPrefs.edit().putString(POS_DETAIL_PASSWORD, password).apply();
    }

    @Override
    public String getPosDetailPosId() {
        return mPrefs.getString(POS_DETAIL_POS_ID, null);
    }

    @Override
    public String getPosDetailAlias() {
        return mPrefs.getString(POS_DETAIL_ALIAS, null);
    }

    @Override
    public String getPosDetailAddress() {
        return mPrefs.getString(POS_DETAIL_ADDRESS, null);
    }

    @Override
    public String getPosDetailPassword() {
        return mPrefs.getString(POS_DETAIL_PASSWORD, null);
    }

    @Override
    public boolean isFirstConfigured() {
        return mPrefs.getBoolean(FIRST_CONFIGURED,false);
    }

    @Override
    public void setFirestConfigured(boolean isChecked) {
         mPrefs.edit().putBoolean(FIRST_CONFIGURED,isChecked).apply();
    }

    @Override
    public void setActiveItemVisibility(boolean isActive) {
        mPrefs.edit().putBoolean(ACTIVE_ITEM_VISIBILITY, isActive).apply();
    }

    @Override
    public boolean getActiveItemVisibility() {
        return mPrefs.getBoolean(ACTIVE_ITEM_VISIBILITY, false);
    }
}
