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
import android.net.Uri;

import com.jim.multipos.utils.AppConstants;
import com.jim.multipos.utils.SecurityTools;

import static com.jim.multipos.utils.managers.LocaleManger.ENGLISH;

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
    private static final String POS_DETAIL_PHONE = "POS_DETAIL_PHONE";
    private static final String FIRST_CONFIGURED = "FIRST_CONFIGURED";
    private static final String ACTIVE_ITEM_VISIBILITY = "ACTIVE_ITEM_VISIBILITY";
    private static final String PRODUCT_LIST_VIEW_TYPE = "PRODUCT_LIST_VIEW_TYPE";
    private static final String FIRST_OPTIONAL_PAYMENT_BUTTON = "FIRST_OPT";
    private static final String SECOND_OPTIONAL_PAYMENT_BUTTON = "SECOND_OPT";
    private static final String EDIT_ORDER_PASSWORD = "count_id";
    private static final String IS_SHOWN = "IS_SHOWN";
    private static final String IS_APP_RUN_FIRST_TIME = "IS_APP_RUN_FIRST_TIME";
    private static final String ORGANIZATION_NAME = "ORGANIZATION_NAME";
    private static final String ORGANIZATION_PHONE_NUMBER = "ORGANIZATION_PHONE_NUMBER";
    private static final String CHECK_WITH_PICTURE = "CHECK_WITH_PICTURE";
    private static final String PICTURE_URI = "PICTURE_URI";
    private static final String DEF_PICTURE = "DEF_PICTURE";
    private static final String PRINT_CHECK_STATE = "PRINT_CHECK_STATE";
    private static final String HINT_CHECK_HINT = "HINT_CHECK_HINT";

    private static final String PRODUCTS_SEC_PROTECTED = "PRODUCTS_SEC_PROTECTED";
    private static final String REPORTS_SEC_PROTECTED = "REPORTS_SEC_PROTECTED";
    private static final String CUSTOMERS_SEC_PROTECTED = "CUSTOMERS_SEC_PROTECTED";
    private static final String INVENTORY_SEC_PROTECTED = "INVENTORY_SEC_PROTECTED";
    private static final String SETTINGS_SEC_PROTECTED = "SETTINGS_SEC_PROTECTED";
    private static final String CASH_MANAGMENT_SEC_PROTECTED = "CASH_MANAGMENT_SEC_PROTECTED";
    private static final String CANCEL_ORDER_SEC_PROTECTED = "CANCEL_ORDER_SEC_PROTECTED";
    private static final String MANUAL_DISCOUNT_SEC_PROTECTED = "MANUAL_DISCOUNT_SEC_PROTECTED";
    private static final String EDIT_ORDER_SEC_PROTECTED = "EDIT_ORDER_SEC_PROTECTED";
    private static final String MANUAL_SERVICE_SEC_PROTECTED = "MANUAL_SERVICE_SEC_PROTECTED";
    private static final String LANGUAGE_CODE = "LANGUAGE_CODE";
    private static final String SERIAL_VALUE = "SERIAL_VALUE";
    private static final String TOKEN_VALUE = "TOKEN_VALUE";
    private static final String OUT_STOCK_CHECK = "OUT_STOCK_CHECK";

    private final SharedPreferences mPrefs;

    public AppPreferencesHelper(Context context) {
        mPrefs = context.getSharedPreferences(AppConstants.PREFERENCE_APPLEVEL, Context.MODE_PRIVATE);
    }


    @Override
    public long getExperedTime() {
        return mPrefs.getLong(EXPERED_TIME, 0);
    }

    @Override
    public void setExperedTime(long experedTime) {
        mPrefs.edit().putLong(EXPERED_TIME, experedTime).apply();
    }

    @Override
    public String getReshreshToken() {
        return mPrefs.getString(PREF_KEY_REFRESH_TOKEN, "");
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        mPrefs.edit().putString(PREF_KEY_REFRESH_TOKEN, refreshToken).apply();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public int getLastPositionCategory() {
        return mPrefs.getInt(LAST_CATEGORY, 0);
    }

    @Override
    public void setLastPositionCategory(int position) {
        mPrefs.edit().putInt(LAST_CATEGORY, position).apply();
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
    public void setPosDetailPhone(String phone) {
        mPrefs.edit().putString(POS_DETAIL_PHONE, phone).apply();
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
    public String getPosDetailPhone() {
        return mPrefs.getString(POS_DETAIL_PHONE, "");
    }


    @Override
    public String getOrganizationName() {
        return mPrefs.getString(ORGANIZATION_NAME, "MULTI POS SOLUTIONS");
    }

    @Override
    public void setOrganizationName(String organizationName) {
        mPrefs.edit().putString(ORGANIZATION_NAME, organizationName).apply();
    }

    @Override
    public String getPosPhoneNumber() {
        return mPrefs.getString(ORGANIZATION_PHONE_NUMBER, "");
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        mPrefs.edit().putString(ORGANIZATION_PHONE_NUMBER, phoneNumber).apply();
    }

    @Override
    public boolean isFirstConfigured() {
        return mPrefs.getBoolean(FIRST_CONFIGURED, false);
    }

    @Override
    public void setFirestConfigured(boolean isChecked) {
        mPrefs.edit().putBoolean(FIRST_CONFIGURED, isChecked).apply();
    }

    @Override
    public void setProductListViewType(int type) {
        mPrefs.edit().putInt(PRODUCT_LIST_VIEW_TYPE, type).apply();
    }

    @Override
    public int getProductListViewType() {
        return mPrefs.getInt(PRODUCT_LIST_VIEW_TYPE, 0);
    }

    @Override
    public void setActiveItemVisibility(boolean isActive) {
        mPrefs.edit().putBoolean(ACTIVE_ITEM_VISIBILITY, isActive).apply();
    }

    @Override
    public boolean getActiveItemVisibility() {
        return mPrefs.getBoolean(ACTIVE_ITEM_VISIBILITY, false);
    }

    @Override
    public double getFirstOptionalPaymentButton() {
        return mPrefs.getFloat(FIRST_OPTIONAL_PAYMENT_BUTTON, 10000);
    }

    @Override
    public void setFirstOptionalPaymentButton(double firstOptionalPaymentButton) {
        mPrefs.edit().putFloat(FIRST_OPTIONAL_PAYMENT_BUTTON, (float) firstOptionalPaymentButton).apply();
    }

    @Override
    public double getSecondOptionalPaymentButton() {
        return mPrefs.getFloat(SECOND_OPTIONAL_PAYMENT_BUTTON, 5000);
    }

    @Override
    public void setSecondOptionalPaymentButton(double secondOptionalPaymentButton) {
        mPrefs.edit().putFloat(SECOND_OPTIONAL_PAYMENT_BUTTON, (float) secondOptionalPaymentButton).apply();
    }

    @Override
    public boolean checkEditOrderPassword(String md5Password) {
        String md5OldPassword = mPrefs.getString(EDIT_ORDER_PASSWORD, SecurityTools.md5("12345"));
//        String md5OldPassword = mPrefs.getString(EDIT_ORDER_PASSWORD,"");
        if (md5OldPassword.equals("")) return false;
        return md5OldPassword.equals(md5Password);
    }

    @Override
    public void setNewEditOrderPassword(String md5Password) {
        mPrefs.edit().putString(EDIT_ORDER_PASSWORD, md5Password).apply();
    }

    @Override
    public boolean isShown() {
        return mPrefs.getBoolean(IS_SHOWN, false);
    }

    @Override
    public void setShowMode(boolean showMode) {
        mPrefs.edit().putBoolean(IS_SHOWN, showMode).apply();
    }

    @Override
    public boolean isAppRunFirstTime() {
        return mPrefs.getBoolean(IS_APP_RUN_FIRST_TIME, true);
    }

    @Override
    public void setAppRunFirstTimeValue(boolean value) {
        mPrefs.edit().putBoolean(IS_APP_RUN_FIRST_TIME, value).apply();
    }

    @Override
    public boolean isPrintPictureInCheck() {
        return mPrefs.getBoolean(CHECK_WITH_PICTURE, true);
    }

    @Override
    public void setPrintPictureInCheck(boolean printPictureInCheck) {
        mPrefs.edit().putBoolean(CHECK_WITH_PICTURE, printPictureInCheck).apply();
    }

    @Override
    public Uri getUriPathCheckPicture() {
        return Uri.parse(mPrefs.getString(PICTURE_URI, ""));
    }

    @Override
    public void setUriPathCheckPicture(Uri uriPathCheckPicture) {
        mPrefs.edit().putString(PICTURE_URI, uriPathCheckPicture.toString()).apply();
        mPrefs.edit().putBoolean(DEF_PICTURE, false).apply();
    }

    @Override
    public boolean isDefaultPicture() {
        return mPrefs.getBoolean(DEF_PICTURE, true);
    }

    @Override
    public boolean isPrintCheck() {
        return mPrefs.getBoolean(PRINT_CHECK_STATE, true);
    }

    @Override
    public void setPrintCheck(boolean printCheck) {
        mPrefs.edit().putBoolean(PRINT_CHECK_STATE, printCheck).apply();
    }


    @Override
    public boolean isProductsProtected() {
        return mPrefs.getBoolean(PRODUCTS_SEC_PROTECTED, false);
    }

    @Override
    public boolean isRepotsProtected() {
        return mPrefs.getBoolean(REPORTS_SEC_PROTECTED, false);

    }

    @Override
    public boolean isCustomersProtected() {
        return mPrefs.getBoolean(CUSTOMERS_SEC_PROTECTED, false);

    }

    @Override
    public boolean isInventoryProtected() {
        return mPrefs.getBoolean(INVENTORY_SEC_PROTECTED, false);

    }

    @Override
    public boolean isSettingsProtected() {
        return mPrefs.getBoolean(SETTINGS_SEC_PROTECTED, false);

    }

    @Override
    public boolean isCashManagmentProtected() {
        return mPrefs.getBoolean(CASH_MANAGMENT_SEC_PROTECTED, false);

    }

    @Override
    public boolean isCancelOrderProtected() {
        return mPrefs.getBoolean(CANCEL_ORDER_SEC_PROTECTED, false);

    }

    @Override
    public boolean isManualDiscountsProtected() {
        return mPrefs.getBoolean(MANUAL_DISCOUNT_SEC_PROTECTED, false);

    }

    @Override
    public boolean isEditOrderProtected() {
        return mPrefs.getBoolean(EDIT_ORDER_SEC_PROTECTED, false);

    }

    @Override
    public boolean isManualServiceFeeProtected() {
        return mPrefs.getBoolean(MANUAL_SERVICE_SEC_PROTECTED, false);
    }

    @Override
    public void setProductsProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(PRODUCTS_SEC_PROTECTED, isProtected).apply();
    }

    @Override
    public void setReportsProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(REPORTS_SEC_PROTECTED, isProtected).apply();

    }

    @Override
    public void setCustomersProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(CUSTOMERS_SEC_PROTECTED, isProtected).apply();

    }

    @Override
    public void setInventorysProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(INVENTORY_SEC_PROTECTED, isProtected).apply();
    }

    @Override
    public void setSettingsProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(SETTINGS_SEC_PROTECTED, isProtected).apply();
    }

    @Override
    public void setCashManagmentsProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(CASH_MANAGMENT_SEC_PROTECTED, isProtected).apply();
    }

    @Override
    public void setCancelOrderProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(CANCEL_ORDER_SEC_PROTECTED, isProtected).apply();
    }

    @Override
    public void setManualDiscountProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(MANUAL_DISCOUNT_SEC_PROTECTED, isProtected).apply();
    }

    @Override
    public void setEditOrderProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(EDIT_ORDER_SEC_PROTECTED, isProtected).apply();
    }

    @Override
    public void setManualServiceFeeProtected(Boolean isProtected) {
        mPrefs.edit().putBoolean(MANUAL_SERVICE_SEC_PROTECTED, isProtected).apply();
    }

    @Override
    public void setHintAbout(Boolean hintAbout) {
        mPrefs.edit().putBoolean(HINT_CHECK_HINT, hintAbout).apply();
    }

    @Override
    public boolean isHintAbout() {
        return mPrefs.getBoolean(HINT_CHECK_HINT, true);
    }

    @Override
    public void setLanguage(String language) {
        mPrefs.edit().putString(LANGUAGE_CODE, language).apply();
    }

    @Override
    public String getLanguageCode() {
        return mPrefs.getString(LANGUAGE_CODE, ENGLISH);
    }

    @Override
    public String getSerialValue() {
        return mPrefs.getString(SERIAL_VALUE, "");
    }

    @Override
    public void setSerialValue(String serialValue) {
        mPrefs.edit().putString(SERIAL_VALUE, serialValue).apply();
    }

    @Override
    public void setRegistrationToken(String token) {
        mPrefs.edit().putString(TOKEN_VALUE, token).apply();
    }

    @Override
    public String getRegistrationToken() {
        return mPrefs.getString(TOKEN_VALUE, "");
    }


}
