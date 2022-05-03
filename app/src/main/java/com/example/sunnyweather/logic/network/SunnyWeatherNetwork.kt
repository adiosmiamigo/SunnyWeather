package com.example.sunnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
object SunnyWeatherNetwork {

    /**定义一个同意的网络数据源访问入口，对所有请求的api进行封装*/
    private val placeService = ServiceCreator.create<PlaceService>()

    /**2 网络层 封装watherService*/
    private val watherService=ServiceCreator.create(WeatherService::class.java)

    suspend fun getDailyWeather(lng:String,lat:String)= watherService.getDailyWeather(lng,lat).await()
    suspend fun getRealWeather(lng:String,lat:String)= watherService.getRealtimeWeather(lng,lat).await()

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    private suspend fun <T> Call<T>.await(): T {
        /**将await()函数定义成了Call<T>的扩展函数，这样
        所有返回值是Call类型的Retrofit网络请求接口就都可以直接调用await()函数了。**/
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    /**如果获得数据，调用resume方法让协程恢复执行*/
                    else continuation.resumeWithException(
                        RuntimeException("resonse body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}