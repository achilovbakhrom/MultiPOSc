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


import android.net.Uri;

/**
 * Created by janisharali on 27/01/17.
 */

public interface PreferencesHelper {

    long getExperedTime();
    void setExperedTime(long experedTime);
    String getReshreshToken();
    void setRefreshToken(String refreshToken);
    String getAccessToken();
    int getLastPositionCategory();
    void setLastPositionCategory(int position);
    int getLastPositionSubCategory(String key);
    void setLastPositionSubCategory(String key, int position);
    int getLastPositionProduct();
    void setLastPositionProduct(int position);
    void setAccessToken(String accessToken);
    void setPosDetailPosId(String posId);
    void setPosDetailAlias(String alias);
    void setPosDetailAddress(String address);
    void setPosDetailPassword(String password);
    void setPosDetailPhone(String phone);
    String getPosDetailPosId();
    String getPosDetailAlias();
    String getPosDetailAddress();
    String getPosDetailPassword();
    String getPosDetailPhone();
    String getOrganizationName();
    void setOrganizationName(String organizationName);
    String getPosPhoneNumber();
    void setPhoneNumber(String phoneNumber);
    boolean isFirstConfigured();
    void setFirestConfigured(boolean isChecked);
    void setProductListViewType(int type);
    int getProductListViewType();
    void setActiveItemVisibility(boolean isActive);
    boolean getActiveItemVisibility();
    double getFirstOptionalPaymentButton();
    void setFirstOptionalPaymentButton(double firstOptionalPaymentButton);
    double getSecondOptionalPaymentButton();
    void setSecondOptionalPaymentButton(double secondOptionalPaymentButton);
    boolean checkEditOrderPassword(String md5Password);
    void setNewEditOrderPassword(String md5Password);
    boolean isShown();
    void setShowMode(boolean showMode);
    boolean isAppRunFirstTime();
    void setAppRunFirstTimeValue(boolean value);
    boolean isPrintPictureInCheck();
    void setPrintPictureInCheck(boolean printPictureInCheck);
    Uri getUriPathCheckPicture();
    void setUriPathCheckPicture(Uri uriPathCheckPicture);
    boolean isDefaultPicture();
    boolean isPrintCheck();
    void setPrintCheck(boolean printCheck);

    //SecurityConfigs
    boolean isProductsProtected();
    boolean isRepotsProtected();
    boolean isCustomersProtected();
    boolean isInventoryProtected();
    boolean isSettingsProtected();
    boolean isCashManagmentProtected();
    boolean isCancelOrderProtected();
    boolean isManualDiscountsProtected();
    boolean isEditOrderProtected();
    boolean isManualServiceFeeProtected();


    void setProductsProtected(Boolean isProtected);
    void setReportsProtected(Boolean isProtected);
    void setCustomersProtected(Boolean isProtected);
    void setInventorysProtected(Boolean isProtected);
    void setSettingsProtected(Boolean isProtected);
    void setCashManagmentsProtected(Boolean isProtected);
    void setCancelOrderProtected(Boolean isProtected);
    void setManualDiscountProtected(Boolean isProtected);
    void setEditOrderProtected(Boolean isProtected);
    void setManualServiceFeeProtected(Boolean isProtected);
    //

    void setHintAbout(Boolean hintAbout);
    boolean isHintAbout();

    void setLanguage(String language);
    String getLanguageCode();

    String getSerialValue();
    void setSerialValue(String serialValue);
    void setRegistrationToken(String token);
    String getRegistrationToken();
    void setOutStockCheck(boolean outStockCheck);
    boolean isOutStockShouldCheck();
}
