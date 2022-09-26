package com.anless.rentmotors.api

interface ResultCallback<T> {
    fun onDataResult(data: T)
    fun onError(code: Int)
}