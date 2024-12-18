package com.absreader.library_books

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.absreader.networks.RetrofitFactory
import com.absreader.networks.dto.library_items.LibraryItemsDTO
import com.absreader.networks.dto.library_items.Result
import com.absreader.networks.services.LibraryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LibraryBooksViewModel {
    val books: MutableLiveData<List<Result>> = MutableLiveData()

    fun getBooks(context: Context, libraryId: String) {
        val client: Retrofit = RetrofitFactory.getInstance(context)
        val call: Call<LibraryItemsDTO> = client.create(LibraryService::class.java)
            .getLibraryItems(libraryId)
        call.enqueue(object : Callback<LibraryItemsDTO> {
            override fun onResponse(
                call: Call<LibraryItemsDTO>,
                response: Response<LibraryItemsDTO>
            ) {
                if (response.isSuccessful) {
                    val libraryItemsDTO: LibraryItemsDTO? = response.body()
                    books.value = libraryItemsDTO?.results
                }
            }

            override fun onFailure(call: Call<LibraryItemsDTO>, t: Throwable) {
                books.value = mutableListOf()
            }
        })
    }
}