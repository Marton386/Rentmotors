package com.anless.rentmotors.utils

sealed class Result<T>{
    class Success<T>(val data: T): Result<T>()
    class Error<T>(val errorCode: Int): Result<T>()
}