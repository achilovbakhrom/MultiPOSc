package com.jim.multipos.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jim.multipos.data.network.oauth2.GetOauthTokenUseCase;
import com.jim.multipos.data.network.oauth2.OAuth2ApiService;
import com.jim.multipos.data.network.oauth2.TokenAuthenticator;
import com.jim.multipos.data.network.oauth2.TokenInterceptor;
import com.jim.multipos.data.prefs.PreferencesHelper;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by developer on 01.08.2017.
 */
@Module
public class MultiPosApiModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(TokenAuthenticator authenticator,TokenInterceptor tokenInterceptor) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (authenticator != null){
            builder.authenticator(authenticator);
        }
        if (tokenInterceptor != null)
            builder.addInterceptor(tokenInterceptor);
        builder.dispatcher(dispatcher);
        builder.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(15 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRestAdapter(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        builder.client(new OkHttpClient())
                .baseUrl("http://192.168.0.109:8081")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        return builder.build();
    }

    @Provides
    @Singleton
    public MultiPosApiService provideMultiPosApiService(Retrofit restAdapter) {
        return restAdapter.create(MultiPosApiService.class);
    }

    @Provides
    @Singleton
    public TokenAuthenticator provideTokenAuthenticator(GetOauthTokenUseCase getOauthTokenUseCase, PreferencesHelper preferencesHelper){
        return new TokenAuthenticator(getOauthTokenUseCase,preferencesHelper);
    }

    @Provides
    @Singleton
    public GetOauthTokenUseCase provideGetOauthTokenUseCase(OAuth2ApiService auth2ApiService,PreferencesHelper preferencesHelper){
        return new GetOauthTokenUseCase(auth2ApiService,preferencesHelper);
    }

    @Provides
    @Singleton
    public OAuth2ApiService provideOAuth2ApiService(){
        OkHttpClient okHttpClient = provideOkHttpClient( null,null);
        Retrofit retrofit = provideRestAdapter(okHttpClient);
        return retrofit.create(OAuth2ApiService.class);
    }

    @Provides
    @Singleton
    public TokenInterceptor provideTokenInterceptor(PreferencesHelper preferencesHelper,GetOauthTokenUseCase getOauthTokenUseCase){
        return new TokenInterceptor(preferencesHelper,getOauthTokenUseCase);
    }
//    @Provides
//    @Singleton
//    public GithubApiService provideGithubApiService(Retrofit restAdapter) {
//        return restAdapter.create(GithubApiService.class);
//    }
//
//    @Provides
//    @Singleton
//    public UserManager provideUserManager(GithubApiService githubApiService) {
//        return new UserManager(githubApiService);
//    }

}
