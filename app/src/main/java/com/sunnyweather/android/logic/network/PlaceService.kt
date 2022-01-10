package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    //@GET注解，当调用searchPlace时，Retrofit自动发送一条GET请求，去访问@GET注解中配置的地址
    //其中query的参数需要动态指定，使用@Query注解来实现，另外两个不变的参数在GET中写下
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    //返回值声明成Call<PlaceResponse>，这样Retrofit就会将服务器返回的JSON数据自动解析成为PlaceResponse对象
    fun searchPlaces(@Query("query") query:String):Call<PlaceResponse>
}