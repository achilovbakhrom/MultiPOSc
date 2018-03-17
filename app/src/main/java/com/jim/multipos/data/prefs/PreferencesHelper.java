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
    String getPosDetailPosId();
    String getPosDetailAlias();
    String getPosDetailAddress();
    String getPosDetailPassword();
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
}
