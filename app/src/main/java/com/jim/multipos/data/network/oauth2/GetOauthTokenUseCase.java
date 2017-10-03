package com.jim.multipos.data.network.oauth2;

import com.jim.multipos.data.prefs.PreferencesHelper;

import java.util.HashMap;
import java.util.Map;


import okhttp3.Credentials;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by developer on 26.08.2017.
 */

public class GetOauthTokenUseCase {
    private final OAuth2ApiService mOauthApiService;
    private PreferencesHelper preferencesHelper;

    public GetOauthTokenUseCase(OAuth2ApiService oauthApiService, PreferencesHelper preferencesHelper) {
        mOauthApiService = oauthApiService;
        this.preferencesHelper = preferencesHelper;
    }

    public void login() {
        String authorizationHeader = Credentials.basic("161885999778BEF863FE37D193F7A", "DEBFFCA522369CB7245E15CDCDBD1");
        Map<String,String> map=new HashMap();
        map.put("grant_type","password");
        map.put("username","name");
        map.put("password","123");
        TokenResponse tokenResponse = mOauthApiService.getToken(map, authorizationHeader)
                .subscribeOn(Schedulers.io()).blockingGet();
        TokenResponse value = tokenResponse;
        preferencesHelper.setAccessToken(value.getAccessToken());
        preferencesHelper.setRefreshToken(value.getRefreshToken());
    }

    public TokenResponse reshreshToken(){
        String authorizationHeader = Credentials.basic("161885999778BEF863FE37D193F7A", "DEBFFCA522369CB7245E15CDCDBD1");
        TokenResponse tokenResponse = mOauthApiService.refreshToken(preferencesHelper.getReshreshToken(), authorizationHeader).subscribeOn(Schedulers.io()).blockingGet();
        return tokenResponse;
    }
}