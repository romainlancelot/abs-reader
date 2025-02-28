package com.absreader.data.network.service

import com.absreader.data.network.dto.audio_book_item_play.DeviceInfoParameters
import com.absreader.data.network.dto.audio_book_item_play.ItemPlayDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AudioBookItemService {
    @DELETE("/api/items/{itemId}")
    fun deleteItem(@Path("itemId") itemId: String): Call<Void>

    @GET("files/{fileName}")
    fun downloadFile(@Path("fileName") fileName: String): Call<ResponseBody>

    @POST("/api/items/{itemId}/play")
    fun getItem(
        @Path("itemId") itemId: String,
        @Query("deviceInfo") deviceInfo: DeviceInfoParameters,
        @Query("forceDirectPlay") forceDirectPlay: Boolean = false,
        @Query("forceTranscode") forceTranscode: Boolean = false,
        @Query("supportedMimeTypes") supportedMimeTypes: List<String> = emptyList(),
        @Query("mediaPlayer") mediaPlayer: String = "unknown"
    ): Call<ItemPlayDTO>
}