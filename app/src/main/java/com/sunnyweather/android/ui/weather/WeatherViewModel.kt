package com.sunnyweather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Location

class WeatherViewModel :ViewModel(){

    private val locationLiveData=MutableLiveData<Location>()

    var locationLng=""
    var locationLat=""
    var placeName=""

    /*调用Transformations.switchMap观察locationLiveData，数据改变就调用Repository.refreshWeather*/
    val weatherLiveData=Transformations.switchMap(locationLiveData){ location->
        Repository.refreshWeather(location.lng,location.lat)
    }

    /*刷新天气信息，并将传入的经纬度参数封装成一个Location对象赋值给locationLiveData*/
    fun refreshWeather(lng:String,lat: String){
        locationLiveData.value= Location(lng,lat)
    }

}