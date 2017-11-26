package com.test.marvel.vallettest.di

import com.google.gson.GsonBuilder
import com.test.marvel.vallettest.BuildConfig
import com.test.marvel.vallettest.network.MarvelRequest
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by victor on 13/11/17.
 *
 */

@Module
class NetworkModule {
    companion object {
        const val NAME_BASE_URL = "NAME_BASE_URL"
    }


    @Provides
    @Named(NAME_BASE_URL)
    fun provideBaseUrlString():String = BuildConfig.MARVEL_BASE_URL

    @Provides
    @Singleton
    fun provideOkhttpClient():OkHttpClient {
        return OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).connectTimeout(10, TimeUnit.SECONDS).build()
    }

    @Provides
    @Singleton
    fun provideGsonConverter(): Converter.Factory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideCallAdapter():RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, converter:Converter.Factory, callAdapterFactory: RxJava2CallAdapterFactory, @Named(NAME_BASE_URL) baseUrl:String):Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addCallAdapterFactory(callAdapterFactory).addConverterFactory(converter).build()
    }


    // ------------------------------------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------------------------------- WEB SERVICES --------------------------------------------------------------------
    @Provides
    @Singleton
    fun provideMarvelRequest(retrofit: Retrofit) = retrofit.create(MarvelRequest::class.java)
}

