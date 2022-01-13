package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

//仓库单例类，一般这里定义的方法，为了能将异步获取的数据以响应式编程的方式通知给上一层，通常返回一个LiveData对象
object Repository {

    //将liveData函数的线程的参数类型指定成Dispatcher.IO，可以把所有代码运行在子线程中
    fun searchPlaces(query:String)= fire(Dispatchers.IO){
            val placeResponse=SunnyWeatherNetwork.searchPlaces(query)
            if(placeResponse.status=="ok"){
                val places=placeResponse.places
                Result.success(places)//包装获取的城市数据列表
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
    }
    /******************************************************************/
    fun refreshWeather(lng:String,lat:String)= fire(Dispatchers.IO) {

            /*coroutineScope函数是挂起函数。可以再任何其他挂起函数中调用。
            * 特点：继承外部协程的作用域并创建一个字协程*/
            coroutineScope {

                /*async必须在协程作用域当中才能调用，他会创建一个新的子协程并返回一个Deferred对象，
                * 如果想要回去async的执行结果，只需要调用Deferred对象的await()方法即可*/
                val deferredRealtime=async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
                }
                val deferredDaily=async {
                    SunnyWeatherNetwork.getDailyWeather(lng, lat)
                }

                //await是得到协程的执行结果
                val realtimeResponse=deferredRealtime.await()
                val dailyResponse=deferredDaily.await()

                if (realtimeResponse.status=="ok"&&dailyResponse.status=="ok"){
                    //将Realtime和Daily对象封装到Weather对象中
                    val weather=Weather(realtimeResponse.result.realtime,
                        dailyResponse.result.daily)
                    Result.success(weather)//包装weather对象
                }else{
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}"+
                                    "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }

    }

    /*这是一个按照liveData函数的参数接受标准定义的高阶函数。再fire函数内部调用liveData()函数，然后
    * 再liveData函数的代码块中统一进行try catch处理，并在try语句中调用传入的Lambda表达式代码，最
    * 终获取的结果调用emit()方法发射出去.
    *
    * 注：再liveData()函数的代码块中，拥有挂起函数的上下文，但是回调到Lambda表达式中，上下文就没了。
    * 所以需要再函数类型前声明一个suspend关键字，表示传入的Lambda表达式中的代码也是有挂起函数上下文的。
    * */
    private fun <T> fire(content:CoroutineContext,block:suspend ()->Result<T>)=
        liveData<Result<T>> {
            val result=try {
                block()
            }catch (e:Exception){
                Result.failure<T>(e)
            }
            emit(result)
        }

    /******************************************************************/
    /*对读写SharedPreferences文件读写进行封装*/
    fun savePlace(place: Place)=PlaceDao.savePlace(place)

    fun getSavedPlace()=PlaceDao.getSavedPlace()

    fun isPlaceSaved()=PlaceDao.isPlaceSaved()
}





/*
object Repository {

    //将liveData函数的线程的参数类型指定成Dispatcher.IO，可以把所有代码运行在子线程中
    fun searchPlaces(query:String)= liveData(Dispatchers.IO){
        val result=try {
            val placeResponse=SunnyWeatherNetwork.searchPlaces(query)
            if(placeResponse.status=="ok"){
                val places=placeResponse.places
                Result.success(places)//包装获取的城市数据列表
            }else{
             Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)//将包装结果发射出去
    }


    fun refreshWeather(lng:String,lat:String)= liveData(Dispatchers.IO) {
        val result=try {

            */
/*coroutineScope函数是挂起函数。可以再任何其他挂起函数中调用。
            * 特点：继承外部协程的作用域并创建一个字协程*//*

            coroutineScope {

                */
/*async必须在协程作用域当中才能调用，他会创建一个新的子协程并返回一个Deferred对象，
                * 如果想要回去async的执行结果，只需要调用Deferred对象的await()方法即可*//*

                val deferredRealtime=async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
                }
                val deferredDaily=async {
                    SunnyWeatherNetwork.getDailyWeather(lng, lat)
                }

                //await是得到协程的执行结果
                val realtimeResponse=deferredRealtime.await()
                val dailyResponse=deferredDaily.await()

                if (realtimeResponse.status=="ok"&&dailyResponse.status=="ok"){

                    //将Realtime和Daily对象封装到Weather对象中
                    val weather=Weather(realtimeResponse.result.realtime,
                                        dailyResponse.result.daily)

                    //包装weather对象
                    Result.success(weather)
                }else{
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}"+
                            "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
        }catch (e:Exception){
            Result.failure<Weather>(e)
        }
        emit(result)//发射包装结果
    }
}*/
