package com.example.sunnyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**Retrofit构建器，可取使用placeService服务*/
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

    //指定所有请求的根路径
    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T>create(serviceClass:Class<T>):T=retrofit.create(serviceClass)
    //泛型在运行时会被类型擦除，但是在inline函数中我们可以指定类型不被擦除，
    // 因为inline函数在编译期会将字节码copy到调用它的方法里，
    // 所以编译器会知道当前的方法中泛型对应的具体类型是什么，
    // 然后把泛型替换为具体类型，从而达到不被擦除的目的，在inline函数中我们可以通过reified关键字来标记这个泛型在编译时替换成具体类型
//这在需要判断泛型类型时十分有用
    inline fun<reified T>create():T=create(T::class.java)



}
