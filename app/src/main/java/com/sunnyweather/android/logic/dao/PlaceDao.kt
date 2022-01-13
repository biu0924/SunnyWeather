package com.sunnyweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

/*Context类中的getSharedPreferences()获取SharedPreferences对象，需要接受两个参数：
* 第一个是用于指定SharedPreferences的文件名称，若不存在则创建一个
* 第二个是指定操作模式，目前只有MODE_PRIVATE可选与传入0相同 ，表只有当前应用程序才可对此文件读写。
*
* 向SharedPreferences存储文件分3步：
* (1)调用SharedPreferences对象的edit获取一个SharedPreferences。Edit对象
* (2)向上面获取的对象中添加数据，例putBoolean，putString()等
* (3)调用apply()方法将添加数据提交*/

object PlaceDao {
    /*将Place对象存储到SharedPreferences文件中，这里使用一个技巧，先通过GSON()将Place对象转化成JSON字符串，
    * 然后就可以用字符串的存储方式保存数据了*/
    fun savePlace(place:Place){
        sharedPreferences().edit{
            putString("place",Gson().toJson(place))
        }
    }

    /*读取是相反的过程，先将JSON字符串从SharedPreferences文件中读取出来，然后再通过GSON将JSON文件解析成Place对象*/
    fun getSavedPlace():Place{
        val placeJson= sharedPreferences().getString("place","")
        return Gson().fromJson(placeJson,Place::class.java)
    }

    /*判单是否有数据已被存储*/
    fun isPlaceSaved()= sharedPreferences().contains("place")

    private fun sharedPreferences()=SunnyWeatherApplication.context.
        getSharedPreferences("sunny_weather",Context.MODE_PRIVATE)

}