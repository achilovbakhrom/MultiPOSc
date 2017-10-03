package com.jim.multipos.data.db.model.servercon;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by developer on 21.08.2017.
 */

public class UserLoginResponse implements Serializable{
    @SerializedName("access_token")
    String access_token;
    @SerializedName("refresh_token")
    String refresh_token;

    public UserLoginResponse(String access_token, String refresh_token) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
