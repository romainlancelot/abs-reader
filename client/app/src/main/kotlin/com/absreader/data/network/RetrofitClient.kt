package com.absreader.data.network

import android.app.Application
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    /*-----------------------------*\
    | Text book API Retrofit Client |
    \*-----------------------------*/

    private const val TEXT_BOOK_API_BASE_URL = "http://10.0.2.2:3000"

    private fun createTextBookApiOkHttpClientWithAuth(application: Application): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val sharedPreferences = application.getSharedPreferences("auth", Context.MODE_PRIVATE)

            val jwt = sharedPreferences.getString("text_book_api_jwt", null)

            val requestBuilder: Request.Builder = chain.request().newBuilder()

            if (!jwt.isNullOrEmpty()) requestBuilder.addHeader("Authorization", "Bearer $jwt")

            chain.proceed(requestBuilder.build())
        }.build()
    }

    fun getTextBookApiInstanceWithAuth(application: Application): Retrofit {
        return Retrofit.Builder().baseUrl(this.TEXT_BOOK_API_BASE_URL).client(createTextBookApiOkHttpClientWithAuth(application)).addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun getTextBookApiInstanceWithoutAuth(application: Application): Retrofit {
        val okHttpClient = OkHttpClient.Builder().build()

        return Retrofit.Builder().baseUrl(this.TEXT_BOOK_API_BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
    }

    /*------------------------------*\
    | Audio book API Retrofit Client |
    \*------------------------------*/

    private const val AUDIO_BOOK_API_BASE_URL = ""

    private fun createAudioBookApiOkHttpClientWithAuth(application: Application): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val sharedPreferences = application.getSharedPreferences("auth", Context.MODE_PRIVATE)

            val jwt = sharedPreferences.getString("audio_book_api_jwt", null)

            val requestBuilder: Request.Builder = chain.request().newBuilder()

            if (!jwt.isNullOrEmpty()) requestBuilder.addHeader("Authorization", "Bearer $jwt")

            chain.proceed(requestBuilder.build())
        }.build()
    }

    fun getAudioBookApiInstanceWithAuth(application: Application): Retrofit {
        return Retrofit.Builder().baseUrl(this.AUDIO_BOOK_API_BASE_URL).client(createTextBookApiOkHttpClientWithAuth(application)).addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun getAudioBookApiInstanceWithoutAuth(): Retrofit {
        val okHttpClient = OkHttpClient.Builder().build()

        return Retrofit.Builder().baseUrl(this.AUDIO_BOOK_API_BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
    }

}
