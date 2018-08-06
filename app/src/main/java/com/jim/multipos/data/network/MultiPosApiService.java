package com.jim.multipos.data.network;

import com.jim.multipos.data.network.model.Signup;
import com.jim.multipos.data.network.model.SignupConfirmationResponse;
import com.jim.multipos.data.network.model.SignupResponse;
import com.jim.multipos.data.network.signing.RegistrationObject;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by developer on 01.08.2017.
 */

public interface MultiPosApiService {

    @POST("/api/v1/register")
    Single<String> getToken(@Body RegistrationObject object);

    @POST("/api/v1/sign-up")
    Single<SignupResponse> signUp(@Body Signup signup);

    @GET("/api/v1/confirm")
    Single<SignupConfirmationResponse> confirmEmail(@Query("email") String email, @Query("access_code") int num);
}
