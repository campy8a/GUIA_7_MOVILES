package com.dhamova.fvr.REST;

import com.dhamova.fvr.DTO.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by JuanCruz on 2/20/17.
 */

public interface ApiService {

    @GET("/news")
    Call<List<News>> getNews();
    @POST("/news")
    Call<ResponseMessage> createNews(@Body News news);

}
