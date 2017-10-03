package com.jim.multipos.data.network.oauth2;

import android.util.Log;

import com.jim.multipos.data.prefs.PreferencesHelper;

import java.io.IOException;
import java.util.Calendar;

import javax.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by developer on 25.08.2017.
 */

public class TokenAuthenticator implements Authenticator {
    private GetOauthTokenUseCase getOauthTokenUseCase;
    private PreferencesHelper preferencesHelper;

    public TokenAuthenticator (GetOauthTokenUseCase getOauthTokenUseCase, PreferencesHelper preferencesHelper){
        this.preferencesHelper = preferencesHelper;
        Log.d("retrofitoaxuth", "TokenAuthenticator: " );

        this.getOauthTokenUseCase = getOauthTokenUseCase;
    }
    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        TokenResponse value = getOauthTokenUseCase.reshreshToken();
        String accessToken = value.getAccessToken();
        preferencesHelper.setAccessToken(value.getAccessToken());
        if(!accessToken.isEmpty())
        return response.request().newBuilder()
                .header("Authorization", String.format("Bearer %s", accessToken))
                .build();
        else return null;
    }
}
