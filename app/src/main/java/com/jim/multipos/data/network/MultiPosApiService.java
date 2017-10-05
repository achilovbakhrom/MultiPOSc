package com.jim.multipos.data.network;

import com.jim.multipos.data.network.signing.RegistrationObject;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by developer on 01.08.2017.
 */

public interface MultiPosApiService {

    @POST("/api/v1/register")
    Single<String> getToken(@Body RegistrationObject object);

}
