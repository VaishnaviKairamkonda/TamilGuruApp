package com.game.TamilGuru.data.api;

import android.content.Context;


import com.game.TamilGuru.utils.Common;
import com.game.TamilGuru.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static RetrofitClient retrofitClient;
    private final Retrofit retrofit;
    private LoginAndRegistrationApiService loginAndRegistrationApiService;


    public static synchronized RetrofitClient getInstance(Context context) {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient(context);
        }
        return retrofitClient;
    }

    private RetrofitClient(Context context) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        String BASE_URL;
        if(Constants.DEVELOPMENT_MODE){
            BASE_URL = Constants.AHA_GURU_STAGING_SERVER;
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        else{
            BASE_URL = Constants.AHA_GURU_PRODUCTION_SERVER;
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public static class ConnectivityInterceptor implements Interceptor {

        private final Context mContext;

        public ConnectivityInterceptor(Context context) {
            mContext = context;
        }

        @Override
        public @NotNull Response intercept(@NotNull Chain chain) throws IOException {
            if (!Common.isInternetConnected(mContext)) {
                throw new NoConnectivityException();
            }

            Request.Builder builder = chain.request().newBuilder();
            return chain.proceed(builder.build());
        }

    }

    public static class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return "No connectivity exception";
        }

    }
    public LoginAndRegistrationApiService getLoginAndRegistrationApis(){
        if(loginAndRegistrationApiService == null)
            loginAndRegistrationApiService = retrofit.create(LoginAndRegistrationApiService.class);
        return loginAndRegistrationApiService;
    }

}
