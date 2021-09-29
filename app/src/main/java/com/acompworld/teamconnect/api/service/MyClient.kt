package com.acompworld.teamconnect.api.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

// I used  this class earlier,when  there was  no involvement of  daggerHilt  (u  can  check  its implementation in test section or u can delete it )
object MyClient {

    //val logging = HttpLoggingInterceptor();

    val okhttpBuilder = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)

    private  val retrofitBuilder = Retrofit.Builder()
        .baseUrl("http://teamconnect.acolabz.com:3001/api/")
        .addConverterFactory(MoshiConverterFactory.create())

    val api = retrofitBuilder
        .client(okhttpBuilder.build())
        .build()
        .create(TeamConnectApi::class.java)
}