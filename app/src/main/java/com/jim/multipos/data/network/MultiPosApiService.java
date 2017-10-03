package com.jim.multipos.data.network;

import com.jim.multipos.ui.registration.TestClass;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by developer on 01.08.2017.
 */

public interface MultiPosApiService {

    @GET("/api/v1/test")
    Single<TestClass> getToken();


}
