package com.absreader.data.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    /*-----------------------------*\
    | Text book API Retrofit Client |
    \*-----------------------------*/

    private const val TEXT_BOOK_API_BASE_URL = "http://localhost:3000";

    private fun createTextBookApiOkHttpClientWithAuth(context: Context): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val sharedPreferences =
                    context.getSharedPreferences("auth", Context.MODE_PRIVATE);

                val jwt =
                    sharedPreferences.getString("text_book_api_jwt", null);

                val requestBuilder: Request.Builder = chain.request().newBuilder();

                if (!jwt.isNullOrEmpty())
                    requestBuilder.addHeader("Authorization", "Bearer $jwt");


                chain.proceed(requestBuilder.build());
            }
            .build();
    }

    fun getTextBookApiInstanceWithAuth(context: Context): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(this.TEXT_BOOK_API_BASE_URL)
            .client(createTextBookApiOkHttpClientWithAuth(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    fun getTextBookApiInstanceWithoutAuth(): Retrofit {
        val okHttpClient = OkHttpClient.Builder().build();

        return Retrofit
            .Builder()
            .baseUrl(this.TEXT_BOOK_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    /*------------------------------*\
    | Audio book API Retrofit Client |
    \*------------------------------*/

    private const val AUDIO_BOOK_API_BASE_URL = "";

    private fun createAudioBookApiOkHttpClientWithAuth(context: Context): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val sharedPreferences =
                    context.getSharedPreferences("auth", Context.MODE_PRIVATE);

                val jwt =
                    sharedPreferences.getString("audio_book_api_jwt", null);

                val requestBuilder: Request.Builder = chain.request().newBuilder();

                if (!jwt.isNullOrEmpty())
                    requestBuilder.addHeader("Authorization", "Bearer $jwt");

                chain.proceed(requestBuilder.build());
            }
            .build();
    }

    fun getAudioBookApiInstanceWithAuth(context: Context): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(this.AUDIO_BOOK_API_BASE_URL)
            .client(createTextBookApiOkHttpClientWithAuth(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    fun getAudioBookApiInstanceWithoutAuth(): Retrofit {
        val okHttpClient = OkHttpClient.Builder().build();

        return Retrofit
            .Builder()
            .baseUrl(this.AUDIO_BOOK_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
}
