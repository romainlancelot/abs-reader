package com.absreader.networks.services

import com.absreader.networks.dto.libraries.LibrariesDTO
import com.absreader.networks.dto.library_items.LibraryItemsDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LibraryService {
    @GET("/api/libraries")
    fun getLibraries(): Call<LibrariesDTO>

    @GET("/api/libraries/{libraryId}/items")
    fun getLibraryItems(@Path("libraryId") libraryId: String): Call<LibraryItemsDTO>
}