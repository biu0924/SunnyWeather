package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
//仓库单例类，一般这里定义的方法，为了能将异步获取的数据以响应式编程的方式通知给上一层，通常返回一个LiveData对象
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
}