package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status:String,val places:List<Place>)
data class Place(val name:String,val loaction:Loaction ,
                       @SerializedName("formateed_address")val address:String)
//让json与kotlin字段建立对应关系，place是个json数组，location是经纬度

data class Loaction(val lng:String,val lat:String)