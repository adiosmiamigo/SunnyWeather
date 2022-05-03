package com.example.sunnyweather.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Respository {

//    fun searchPlaces(query:String)= liveData(Dispatchers.IO){
//        /*DispatcherIO使得当下所有代码都运行在子进程中**/
//        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
//        val result=try{
//            val placeRespnse= SunnyWeatherNetwork.searchPlaces(query)
//            if(placeRespnse.status=="ok"){
//                val places=placeRespnse.places
//                Result.success(places)
//            } else {
//              Result.failure(RuntimeException("response status is ${placeResponse.status}"))
//            }
//        }    catch(e:Exception){
//            Result.failure<List<Place>>(e)
//        }
//        emit(result)
//    }

//    fun refreshWeather(lng:String,lat:String)=liveData(Dispatchers.IO){
//        val result=try{
//            coroutineScope {
//                val deferredRealtime=async{
//                    SunnyWeatherNetwork.getRealWeather(lng,lat)
//                }
//                val deferredDaily=async{
//                    SunnyWeatherNetwork.getDailyWeather(lng,lat)
//                }
//                val realtimeResponse=deferredRealtime.await()
//                val dailyResponse=deferredDaily.await()
//                /**async阻塞进程直到获得两个协程的结果*/
//                if(realtimeResponse.status=="ok"&&dailyResponse.status=="ok"){
//                    /**把得到的两个返回结果丢入封装好的weather数据类**/
//                    val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
//                    Result.success(weather)
//                }else{
//                    Result.failure(
//                        RuntimeException(
//                            "realtime resonse status is ${realtimeResponse.status}"+
//                                    "daily response status is ${dailyResponse.status}"
//                        )
//                    )
//                }
//            }
//        }catch (e:java.lang.Exception)
//        {
//            Result.failure<Weather>(e)
//        }
//        emit(result)
//    }

    /**2改善：之前必须对每个网络请求都处理try catch处理，现在封装在同意出口函数进行一次try catch*/
    /**suspend:()->声明lambda表达式里是有挂起函数上下文的*/
    private fun<T>fire(context:CoroutineContext,block:suspend()->Result<T>)=
        liveData<Result<T>>(context){
            val result=try{
                block()
            }catch (e:Exception){
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun searchPlaces(query:String)= fire(Dispatchers.IO) {
        /*DispatcherIO使得当下所有代码都运行在子进程中**/
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if(placeResponse.status=="ok"){
            val places=placeResponse.places
            Result.success(places)
        }else{
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }

    }

    fun refreshWeather(lng:String,lat:String)=fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            /**async阻塞进程直到获得两个协程的结果*/
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                /**把得到的两个返回结果丢入封装好的weather数据类**/
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime resonse status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

}
