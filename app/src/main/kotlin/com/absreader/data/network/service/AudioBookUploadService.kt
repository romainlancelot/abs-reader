package com.absreader.data.network.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AudioBookUploadService {
    @Multipart
    @POST("/api/upload")
    fun uploadBook(
        @Part file: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("author") author: RequestBody,
        @Part("series") series: RequestBody,
        @Part("library") library: RequestBody,
        @Part("folder") folder: RequestBody
    ): Call<Void>
}