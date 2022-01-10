package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    //使用ServiceCreator创建一个PlaceService接口的动态对象
    private val placeService=ServiceCreator.create<PlaceService>()
    //定义searchPlace函数，调用PlaceService接口中定义的searchPlaces（方法，发起搜索城市数据请求
    suspend fun searchPlaces(query:String)= placeService.searchPlaces(query).await()

    /*await()是一个挂起函数，给他声明一个泛型T，将await()定义成Call<T>的拓展函数，这样所有返回值是Call类型的
    retrofit网络请求接口就都可以直接调用await函数了*/
    private suspend fun <T> Call<T>.await():T{
        /*suspendCoroutine（挂起协程）必须在协程作用域或者挂起作用域才能调用，接受一个Lambda表达式，将当前协程挂起*/
        return suspendCoroutine { continuation ->
            /*由于拓展函数的原因，我们现在拥有Call对象的上下文。可以直接调用enqueue方法让Retrofit发起网络请求*/
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    /*请求成功，调用Continuation的resume()方法恢复被挂起的协程，并传入服务器响应的数据，
                    该值会成为suspendCoroutine的返回值
                    若请求失败，则调用resumeWithException恢复被挂起协程并且传入异常原因
                    */
                    if(body!=null)
                        continuation.resume(body)
                    else
                        continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}