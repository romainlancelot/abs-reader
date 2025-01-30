package com.absreader.data.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AudioBookRetrofitClient {
    companion object {
        fun getInstance(context: Context, baseUrl: String = ""): Retrofit {
            val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            val server: String = sharedPreferences.getString("audio_book_api_server_url", baseUrl).toString()
            val token = sharedPreferences.getString("audio_book_api_jwt", "").toString()
            val clientBuilder: Builder = Builder()
            if (token.isNotEmpty()) {
                clientBuilder.addInterceptor(AuthInterceptor(token))
            }
            val client: OkHttpClient = clientBuilder.build()
            return Retrofit.Builder().baseUrl(server).client(client).addConverterFactory(GsonConverterFactory.create()).build()
        }
    }

    private class AuthInterceptor(private val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder().addHeader("Authorization", token).build()
            return chain.proceed(request)
        }
    }
}