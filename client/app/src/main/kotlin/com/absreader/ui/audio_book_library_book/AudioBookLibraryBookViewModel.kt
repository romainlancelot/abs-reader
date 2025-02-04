package com.absreader.ui.audio_book_library_book

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.absreader.data.network.AudioBookRetrofitClient
import com.absreader.data.network.dto.audio_book_library_items.LibraryItemsDTO
import com.absreader.data.network.dto.audio_book_library_items.Result
import com.absreader.data.network.service.AudioBookLibraryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AudioBookLibraryBookViewModel {
    val books: MutableLiveData<List<Result>> = MutableLiveData()

    fun getBooks(context: Context, libraryId: String) {
        val client: Retrofit = AudioBookRetrofitClient.getInstance(context)
        val call: Call<LibraryItemsDTO> = client.create(AudioBookLibraryService::class.java)
            .getLibraryItems(libraryId)
        call.enqueue(object : Callback<LibraryItemsDTO> {
            override fun onResponse(
                call: Call<LibraryItemsDTO>,
                response: Response<LibraryItemsDTO>
            ) {
                if (response.isSuccessful) {
                    val libraryItemsDTO: LibraryItemsDTO? = response.body()
                    val audioBooks: MutableList<Result> = mutableListOf()
                    for (libraryItem: Result in libraryItemsDTO?.results ?: mutableListOf()) {
                        if (libraryItem.media.numAudioFiles == 0) {
                            continue
                        }
                        if (libraryItem.media.coverPath != null) {
                            libraryItem.media.coverPath =
                                client.baseUrl().toString() + "/api/items/${libraryItem.id}/cover"
                        }
                        audioBooks.add(libraryItem)
                    }
                    books.value = audioBooks
                }
            }

            override fun onFailure(call: Call<LibraryItemsDTO>, t: Throwable) {
                books.value = mutableListOf()
            }
        })
    }
}