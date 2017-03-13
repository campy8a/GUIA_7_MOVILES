package com.dhamova.fvr.REST;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JuanCruz on 2/20/17.
 */

public class RestClient {
    private static final String BASE_URL = "http://demo7643885.mockable.io/";
    private ApiService apiService;
    private static RestClient instance;
    private RestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) .addConverterFactory(GsonConverterFactory.create()) .build();
        apiService = retrofit.create(ApiService.class);
    }
    public ApiService getApiService() {
        return apiService;
    }
    public static RestClient getInstance() {
        if (instance == null) {
        instance = new RestClient(); }
        return instance;
    }
}