package com.example.sunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Respository
import com.example.sunnyweather.logic.model.Loaction

class WeatherViewModel :ViewModel(){
    private val locationLiveData=MutableLiveData<Loaction>()
    /**lng,lat是经纬度*/
    var loactionLng=""
    var locationLat=""
    var placeName=""
    val weatherLiveData= Transformations.switchMap(locationLiveData){
        location->Respository.refreshWeather(location.lng,location.lat)

    }
    fun refreshWeather(lng:String,lat:String){
        locationLiveData.value=Loaction(lng,lat)
    }
}