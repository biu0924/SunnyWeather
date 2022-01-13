package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    //实时天气
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng:String,@Path("lat") lat:String): Call<RealtimeResponse>
    //未来天气
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng:String,@Path("lat") lat:String): Call<DailyResponse>
    //使用@Path向请求接口中动态传入经纬度坐标，返回值分别是Call<RealtimeResponse>和Call<DailyResponse>
}