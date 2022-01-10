package com.sunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL="https://api.caiyunapp.com/"
    private val retrofit=Retrofit.Builder()//baseurl和addconcerterfactory必须调用
        .baseUrl(BASE_URL)//指定所有Retrofit请求的根路径
        .addConverterFactory(GsonConverterFactory.create())//指定Retrofit在解析数据时所使用的转换库、
        .build()
    // 提供外部可见的create方法，接受class类型参数，外部调用这个方法时，
    // 实际上就是调用了Retrofit对象的create方法，从而构建出Service接口的动态代理对象
    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)
    //又定义一个不带参数的create方法，使用inline修饰方法（内联函数），用reified（具体化）修饰泛型，得到使用泛型类型的Class
    inline fun <reified T> create(): T = create(T::class.java)
}