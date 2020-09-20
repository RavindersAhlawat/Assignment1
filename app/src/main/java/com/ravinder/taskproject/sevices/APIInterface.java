package com.ravinder.taskproject.sevices;

import com.ravinder.taskproject.ModelClass.Datum;
import com.ravinder.taskproject.ModelClass.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface APIInterface {
    @GET("get-photo/search")
    Call<Example> getAPIResponse(@Header("Authorization") String token);

}
