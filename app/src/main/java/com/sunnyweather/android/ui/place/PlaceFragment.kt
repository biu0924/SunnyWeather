package com.sunnyweather.android.ui.place

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.R
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment:Fragment() {

    //运用懒加载技术活去PlaceViewModel实例，允许在整个类中随时使用viewModel变量，无需关心它何时初始化
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter

    //加载fragment_place布局
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place,container,false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //设置LayoutManager
        val layoutManager=LinearLayoutManager(activity)
        recyclerView.layoutManager=layoutManager

        //设置适配器,讲placeList作为数据源
        adapter=PlaceAdapter(this,viewModel.placeList)
        recyclerView.adapter=adapter

        //使用addTextChangedListener方法监听搜索框内容的变化情况
        searchPlaceEdit.addTextChangedListener { editable->
            val content=editable.toString()

            /*搜索框内容发生变化，就获取新内容，然后传递给searchPlace方法，发起搜索城市数据网络请求
            * 搜索框内容为空时，将RecyclerView隐藏起来，同时将仅用于美观的背景图片显示出来*/
            if (content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                recyclerView.visibility=View.GONE
                bgImageView.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        /*解决了搜索城市数据发起的请求，还要能获取到服务器相应的数据才行，借助LiveData完成。*/
        /*观察viewModel的placeLiveData，任何数据发生变化，就会回调到传入的Obeserver接口实现中*/
        viewModel.placeLiveData.observe(this, Observer { result->
            val places=result.getOrNull()

            /*如果数据不为空，将数据添加到PlaceViewModel的placeList集合中，并通知PlaceAdapter刷新界面；
            * 如果为空，弹出异常*/
            if(places!=null){
                recyclerView.visibility=View.VISIBLE
                bgImageView.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}