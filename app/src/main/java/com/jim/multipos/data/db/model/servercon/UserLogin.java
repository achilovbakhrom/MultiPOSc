package com.jim.multipos.data.db.model.servercon;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.http.Body;
import retrofit2.http.Query;

/**
 * Created by developer on 21.08.2017.
 */

public class UserLogin implements Serializable{

    @SerializedName("grant_type")
     String grantType;
    @SerializedName("username")
     String username;
    @SerializedName("password")
     String password;
    @SerializedName("client_id")
     String client_id;
    @SerializedName("client_secret")
     String secret_id;

    public UserLogin(String grantType, String username, String password, String client_id, String secret_id) {
        this.grantType = grantType;
        this.username = username;
        this.password = password;
        this.client_id = client_id;
        this.secret_id = secret_id;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getSecret_id() {
        return secret_id;
    }

    public void setSecret_id(String secret_id) {
        this.secret_id = secret_id;
    }
}
