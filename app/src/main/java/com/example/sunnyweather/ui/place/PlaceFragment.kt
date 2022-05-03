package com.example.sunnyweather.ui.place

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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.R
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {
/**lazy懒加载技术，允许viewModel随时随地进行初始化*/
    val viewModel by lazy { ViewModelProviders.of(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter:PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager=LinearLayoutManager(activity)
        recyclerView.layoutManager=layoutManager
/**以placelist作为recycler的数据源*/
        adapter= PlaceAdapter(this,viewModel.placeList)
        recyclerView.adapter=adapter

        searchPlaceEdit.addTextChangedListener{editable->
            val content =editable.toString()
            /**监听搜索输入框内的文字，当搜索框内容为空就隐藏recyclerview显现背景图片*/
            if(content.isNotEmpty())
            {
                viewModel.searchPlaces(content)
            }else{
                recyclerView.visibility=View.GONE
                bgImgView.visibility=View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }

        }
        /**observer监控数据变化*/
        viewModel.placeLiveData.observe(this, Observer{
            result->
            val places=result.getOrNull()
            if(places!=null){
                recyclerView.visibility=View.VISIBLE
                bgImgView.visibility=View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

}