package com.example.session.http

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

public interface RequestService {
    @GET("feed")
    fun getFeeds(): Call<Object>;
}