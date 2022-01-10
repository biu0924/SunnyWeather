package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName


//三个数据类，分别代表接口返回的json格式定义，状态，地名，经纬度
data class PlaceResponse(val status:String,val places:List<Place>)
//使用SerializedName注解是json字段和kotlin建立联系
//Place是一个json数组，包含几个与查询相关度较高的地区信息
data class Place(val name:String,val location:Location,
                 @SerializedName("formatted_address") val lat:String)

data class Location(val lng:String,val lat: String)