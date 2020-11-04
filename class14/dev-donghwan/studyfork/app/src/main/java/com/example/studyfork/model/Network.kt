package com.example.studyfork.model

import com.example.studyfork.api.NaverApi
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    private const val clientId = "7et7ziEIFBngKSf4WOri"
    private const val clientSecret = "1w6dwTIT7D"

    val naverApi: NaverApi by lazy { retrofit.create(NaverApi::class.java) }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(client)
            .build()
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor { chain ->
            chain.request()
                .newBuilder()
                .addHeader("X-Naver-Client-Id", clientId)
                .addHeader("X-Naver-Client-Secret", clientSecret)
                .build()
                .let { request ->
                    chain.proceed(request)
                }
        }.build()
    }
}