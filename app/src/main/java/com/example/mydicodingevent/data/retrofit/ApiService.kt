package com.example.mydicodingevent.data.retrofit

import com.example.mydicodingevent.data.response.EventDetailResponse
import com.example.mydicodingevent.data.response.ResponseEvents
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getUpcomingEvents(
        @Query("active")active: Int = 1
    ): Call<ResponseEvents>

    @GET("events")
    fun getFinishedEvents(
        @Query("active")active: Int = 0
    ): Call<ResponseEvents>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: Int):Call<EventDetailResponse>

    @GET("events")
    fun getSearchEvent(@Query("active") active: Int = -1, @Query("q") keyword: String) : Call<ResponseEvents>

    @GET("events")
    fun getEvents(@Query("active")active: Int = 0) : Call<List<ResponseEvents>>


}