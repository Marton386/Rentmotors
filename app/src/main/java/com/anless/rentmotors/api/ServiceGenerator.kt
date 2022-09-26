package com.anless.rentmotors.api

import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private const val BASE_URL = "https://api.onlinefleet.ru"
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
    private var retrofit = builder.build()

    private val logging = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val headerInterceptor = HeaderInterceptor.getInstance()
    private val httpClient = OkHttpClient.Builder()

    fun <S> createService(serviceClass: Class<S>): S {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging)
            builder.client(httpClient.build())
            retrofit = builder.build()
        }
        if (!httpClient.interceptors().contains(headerInterceptor)) {
            httpClient.addInterceptor(headerInterceptor)
            builder.client(httpClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }
}