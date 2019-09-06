package com.example.session.http

import com.google.gson.internal.LinkedTreeMap
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class RequestServiceImpl {
    val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.56.1:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service = retrofit.create(RequestService::class.java);

    fun getFeeds() {
        service.getFeeds().enqueue(object: Callback<Object>{
            override fun onFailure(call: Call<Object>, t: Throwable) {
                println(t)
            }

            override fun onResponse(call: Call<Object>, response: Response<Object>) {
                (response.body() as ArrayList<LinkedTreeMap<String, Object>>).let{
                    val map = it.get(0)
                    (map.get("feeds") as ArrayList<LinkedTreeMap<String, Object>>).let{
                        it.forEach {
                            val name = it.get("name") as String
                            val active = it.get("active") as Boolean
                            println("name: ${name}, active: ${active.toString()}")
                        }
                    }
                }
            }
        })
    }
}