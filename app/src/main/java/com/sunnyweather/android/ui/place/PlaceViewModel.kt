package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel:ViewModel() {

    /*MutableLiveData是一种可变的LiveData,有三种读写数据方法，
    分别是get、set(只可在主线程调用)和post(再非主线程给LiveData设置数据)Value()方法*/
    //private，保证数据封装性，外部使用时候，只观察特定的LiveData即可，如想要查询的部分
    private val searchLiveData=MutableLiveData<String>()

    /*placeList用于对界面上显示的城市数据进行缓存，因为原则上与界面相关的数据都应该放到ViewModel中，这样可以
    * 保证他们在手机屏幕发生旋转的时候不会丢失*/
    val placeList=ArrayList<Place>()

    /*switMap()接受两个参数：第一个是新增的livedata对象，使用该方法对它观察；第二个是转换函数，这个函数必须
    * 返回一个LiveData对象，因为switchMap()的工作原理就是将转换函数中返回的LiveData对象转换成另一个可观察的
    * LiveData对象。*/
    val placeLiveData=Transformations.switchMap(searchLiveData){query->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query:String){
        searchLiveData.value=query
    }

    /*梳理：
    * 当外部调用searchPlace方法时候，不发起任何请求或函数调用，只会讲传入的query设置到searchLiveData
    * 中，一旦searchLiveData的数据发生变化，那么观察它的switchMap就会执行，并且调用转换函数。然后转换
    * 函数调用Repository.searchPlaces(query)就可以发起网络请求。同时，switchMap方法将
    * Repository.searchPlaces(query)返回的LiveData转换成一个可观察的LiveData对象*/

    /***********************************************************************************/
    /*调用仓库层的接口*/
    fun savePlace(place: Place)= Repository.savePlace(place)

    fun getSavedPlace()= Repository.getSavedPlace()

    fun isPlaceSaved()= Repository.isPlaceSaved()
}