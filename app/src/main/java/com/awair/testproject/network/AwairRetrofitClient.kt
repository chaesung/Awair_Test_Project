package com.awair.testproject.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object AwairRetrofitClient {

    private const val TIMEOUT_LIMIT = 5000.toLong()

    inline fun <reified T> createNetworkClient(
        baseUrl: String,
        debug: Boolean = true
    ): T = retrofitClient(baseUrl, httpClient(debug))

    fun httpClient(debug: Boolean): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val clientBuilder = OkHttpClient.Builder()
        if (debug) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return clientBuilder
            .addNetworkInterceptor(httpLoggingInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(TIMEOUT_LIMIT, TimeUnit.MILLISECONDS)
            .connectTimeout(TIMEOUT_LIMIT, TimeUnit.MILLISECONDS)
            .build()
    }

    inline fun <reified T>retrofitClient(baseUrl: String, httpClient: OkHttpClient): T =
        Retrofit.Builder().baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(T::class.java)
}