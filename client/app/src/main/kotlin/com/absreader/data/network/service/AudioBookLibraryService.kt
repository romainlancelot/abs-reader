package com.absreader.data.network.service

import com.absreader.data.network.dto.audio_book_libraries.LibrariesDTO
import com.absreader.data.network.dto.audio_book_library_items.LibraryItemsDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AudioBookLibraryService {
    @GET("/api/libraries")
    fun getLibraries(): Call<LibrariesDTO>

    @GET("/api/libraries/{libraryId}/items")
    fun getLibraryItems(@Path("libraryId") libraryId: String): Call<LibraryItemsDTO>
}