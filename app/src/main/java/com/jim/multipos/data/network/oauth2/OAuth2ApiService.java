package com.jim.multipos.data.network.oauth2;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by developer on 01.08.2017.
 */

public interface OAuth2ApiService {


     @FormUrlEncoded
     @POST("/oauth/token?grant_type=password")
     @Headers({
             "Accept: application/json"
     })
     Single<TokenResponse> getToken(@FieldMap Map<String, String> map, @Header("Authorization") String auth);


     @FormUrlEncoded
     @POST("/oauth/token?grant_type=refresh_token")
     @Headers({
             "Accept: application/json"
     })
     Single<TokenResponse> refreshToken(@Field("refresh_token") String refreshToken, @Header("Authorization") String auth);



}
