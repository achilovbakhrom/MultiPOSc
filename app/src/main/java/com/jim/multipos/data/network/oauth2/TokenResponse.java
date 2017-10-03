package com.jim.multipos.data.network.oauth2;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by developer on 25.08.2017.
 */

public class TokenResponse implements Serializable {
     @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in") 
    private long expiredTime;

    @SerializedName("token_type") 
    private String tokenType;

    @SerializedName("refresh_token") 
    private String refreshToken;

    public TokenResponse(){

    }
    public TokenResponse(String accessToken, long expiredTime, String tokenType, String refreshToken) {
        this.accessToken = accessToken;
        this.expiredTime = expiredTime;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
