package com.ferit.filipcesnek.dotaapp.DotaApi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Networking{
    val dotaApiSearchService: DotaApi = Retrofit.Builder()
        .addConverterFactory(ConverterFactory.converterFactory)
        .client(HttpClient.client)
        .baseUrl(BASE_URL)
        .build()
        .create(DotaApi::class.java)
}

object ConverterFactory{
    val converterFactory = GsonConverterFactory.create()
}

object HttpClient{
    val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
            .readTimeout(5, TimeUnit.MINUTES) // read timeout
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}
