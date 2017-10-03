package com.jim.multipos.data.network.oauth2;

import android.content.SharedPreferences;
import android.util.Log;

import com.jim.multipos.data.prefs.PreferencesHelper;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by developer on 25.08.2017.
 */

public class TokenInterceptor implements Interceptor {
    private PreferencesHelper preferencesHelper;
    private GetOauthTokenUseCase getOauthTokenUseCase;

    public TokenInterceptor(PreferencesHelper preferencesHelper,GetOauthTokenUseCase getOauthTokenUseCase){

        this.preferencesHelper = preferencesHelper;
        this.getOauthTokenUseCase = getOauthTokenUseCase;
    }
    @Override
    public synchronized  Response intercept(Chain chain) throws IOException {
        Request newRequest=chain.request();



//        //get expire time from shared preferences
//        long expireTime=preferencesHelper.getExperedTime();
//
//
//        /**
//         * when comparing dates -1 means date passed so we need to refresh token
//         * see {@link Date#compareTo}
//         */
//        if(expireTime<System.currentTimeMillis()) {
//            TokenResponse tokenInterceptor =  getOauthTokenUseCase.getObservable().toBlocking().value();
//            preferencesHelper.setExperedTime(tokenInterceptor.getExpiredTime()*1000+System.currentTimeMillis());
//
//            String accessToken = tokenInterceptor.getAccessToken();
//            Log.d("retrofitoaxuth", "intercept: " + accessToken);
//            Log.d("retrofitoaxuth", "intercept: " + tokenInterceptor.getRefreshToken());
//            preferencesHelper.setAccessToken(tokenInterceptor.getAccessToken());
//            newRequest=chain.request().newBuilder()
//                    .header("Authorization", tokenInterceptor.getAccessToken())
//                    .build();
//        }
        if(!preferencesHelper.getAccessToken().isEmpty())
            newRequest=chain.request().newBuilder()
                    .header("Authorization", String.format("Bearer %s", preferencesHelper.getAccessToken()) )
                    .build();
        return chain.proceed(newRequest);
    }
}
