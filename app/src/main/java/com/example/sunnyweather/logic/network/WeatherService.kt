package com.example.sunnyweather.logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.DailyResponse
import com.example.sunnyweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    /**使用@Path动态传入参数*/
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lagt)/realtime.json")
    fun getRealtimeWeather(@Path("lng")lng:String,@Path("lat")lat:String):
            Call<RealtimeResponse>
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lagt)/daily.json")
    fun getDailyWeather(@Path("lng")lng:String,@Path("lat")lat:String):
    Call<DailyResponse>
}