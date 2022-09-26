package com.anless.rentmotors.api

import java.util.*
import okhttp3.Response
import okhttp3.Interceptor

class HeaderInterceptor private constructor() : Interceptor {
    companion object {
        private var instance: HeaderInterceptor? = null
        private const val TOKEN = "8cSlCB7jRxAfouGaTLkk1fxXgZLp8qmV"
        private val LNG = if (Locale.getDefault().language == "ru") "RU" else "EN"

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: HeaderInterceptor().also { instance = it }
            }
    }

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("x-token", TOKEN)
                .addHeader("x-lng", LNG)
                .build()
        )
    }
}