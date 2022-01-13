package com.sunnyweather.android.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*


//适配器继承于RecyclerView.Adapter,泛型指定为PlaceAdapter.ViewHolder，其中ViewHolder是内部类
class PlaceAdapter(private val fragment:PlaceFragment,private val placeList:List<Place>):
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
        val holder=ViewHolder(view)

        /*给place_item.xml最外层布局设置监听器，在点击事件中获取当前点击项的经纬度坐标和地区名称，传入到Intent中*/
        holder.itemView.setOnClickListener {
            val position=holder.adapterPosition
            val place=placeList[position]

            /*对PlaceFragment所处的Activity进行判断：
            * 如果在WeatherActivity中，那么关闭滑动菜单，给WeatherActivity赋值新的经纬度和地名，并刷新天气
            * 如果在MainActivity中，保持不变*/
            val activity=fragment.activity
            if (activity is WeatherActivity){
                activity.drawerLayout.closeDrawers()
                activity.viewModel.locationLng=place.location.lng
                activity.viewModel.locationLat=place.location.lat
                activity.viewModel.placeName=place.name
                activity.refreshWeather()
            }else {
                val intent = Intent(parent.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
                fragment.activity?.finish()
            }
            //存储选中城市
            fragment.viewModel.savePlace(place)
        }
        return holder
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