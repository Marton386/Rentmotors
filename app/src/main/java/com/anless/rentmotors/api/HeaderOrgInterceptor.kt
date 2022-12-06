package com.anless.rentmotors.api

import okhttp3.Response
import okhttp3.Interceptor

class HeaderOrgInterceptor private constructor() : Interceptor {
    companion object {
        private var instance: HeaderOrgInterceptor? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: HeaderOrgInterceptor().also { instance = it }
            }
    }

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(request().newBuilder().build())
    }
}