package com.norkut.orkesta_mobile.repository.service

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.norkut.orkesta_mobile.common.converters.DateTimeConverter
import com.norkut.orkesta_mobile.common.utils.Environment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ServiceBuilder @Inject constructor() {

    companion object{

        val env = Environment()
        private val isProduction = env.isProduction
        private val REMOTE_API_URL = env.remoteUrl
        private var LOCAL_API_URL = env.localUrl




        fun <Api> buildApi(
            api: Class<Api>,
            isLocal: Boolean = false
        ): Api {
            val serviceApi = ServiceConstants.findApi(api, isProduction)
            val apiUrl = "${isLocal.apiUrl()}:${serviceApi.port}/${serviceApi.suffix}"
            return buildRetrofitClient(apiUrl).create(api)
        }


        private fun buildRetrofitClient(apiUrl: String): Retrofit =
            Retrofit.Builder()
                .baseUrl(apiUrl)
                .client(buildHttpClient())
                .addConverterFactory(GsonConverterFactory.create(buildConverter()))
                .build()



        private fun buildHttpClient() = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor())
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()


        private fun loggingInterceptor(): HttpLoggingInterceptor {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return logging
        }


        private fun Boolean.apiUrl() = if (this) LOCAL_API_URL else REMOTE_API_URL



        private fun buildConverter(): Gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, DateTimeConverter())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

    }






}