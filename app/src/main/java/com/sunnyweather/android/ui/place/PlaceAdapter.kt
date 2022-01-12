package com.sunnyweather.android.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place


//适配器继承于RecyclerView.Adapter,泛型指定为PlaceAdapter.ViewHolder，其中ViewHolder是内部类
class PlaceAdapter(private val fragment:Fragment,private val placeList:List<Place>):
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    /*内部类，继承RecyclerView.ViewHolder，他的主构造函数要传入一个View参数（通常
    是RecyclerView子项的最外层布局，然后获取实例），*/
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val placeName:TextView=view.findViewById(R.id.placeName)
        val placeAddress:TextView=view.findViewById(R.id.placeAddress)
    }

    //创建ViewHolder实例，将place_item布局加载进来，创建ViewHolder实例
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        return ViewHolder(view)
    }

    //对RecyclerView子项数据进行赋值，会每个子项被滚动到屏幕内的时候执行，用position得到当前place实例
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place=placeList[position]
        holder.placeName.text=place.name
        holder.placeAddress.text=place.address
    }

    //告诉RecyclerView一共有多少子项
    override fun getItemCount()=placeList.size
}