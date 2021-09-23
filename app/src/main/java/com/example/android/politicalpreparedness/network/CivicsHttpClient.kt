package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CivicsHttpClient : OkHttpClient() {

    companion object {

        const val API_KEY =
            "AIzaSyDFM2rWBl7VeLo998ZbZjg6dwrUUaLmZbQ" //TODO: Place your API Key Here

        fun getClient(): OkHttpClient {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY
            return Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original
                        .url
                        .newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build()
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()
                    chain.proceed(request)
                }
                .addInterceptor(logger)
                .build()
        }

    }

}