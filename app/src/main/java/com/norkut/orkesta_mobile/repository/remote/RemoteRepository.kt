package com.norkut.orkesta_mobile.repository.remote

import retrofit2.Response
import java.lang.Exception

abstract class RemoteRepository{
    fun<T> Response<T>.validate(customException: java.lang.Exception? = null) : T? {
        if (isSuccessful) return body()
        else throw customException ?: Exception(message())
    }
}