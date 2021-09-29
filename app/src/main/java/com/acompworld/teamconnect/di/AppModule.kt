package com.acompworld.teamconnect.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.acompworld.teamconnect.api.EnumConverterFactory
import com.acompworld.teamconnect.api.service.TeamConnectApi
import com.acompworld.teamconnect.repo.TeamConnectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit

import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

      @Provides
      @Singleton
    fun provideOkhttpClient() = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun provideRetrofit(client : OkHttpClient) = Retrofit.Builder()
        .client(client)
        .baseUrl("http://teamconnect.acolabz.com:3001/api/")
    //    .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(EnumConverterFactory())
        .build()
        .create(TeamConnectApi::class.java)


    @Provides
    @Singleton
    fun provideRepo(api: TeamConnectApi) = TeamConnectRepository(api)

    @Provides
    @Singleton
    fun provideConnectivityManager(app: Application) =
        app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

}