package com.absreader.ui.audio_book_library

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.absreader.data.network.AudioBookRetrofitClient
import com.absreader.data.network.dto.audio_book_libraries.LibrariesDTO
import com.absreader.data.network.dto.audio_book_libraries.Library
import com.absreader.data.network.service.AudioBookLibraryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AudioBookLibraryViewModel {

    val libraries: MutableLiveData<MutableList<Library>> = MutableLiveData()

    fun getLibraries(context: Context) {
        val client: Retrofit = AudioBookRetrofitClient.getInstance(context)
        val call: Call<LibrariesDTO> = client.create(AudioBookLibraryService::class.java).getLibraries()
        call.enqueue(object : Callback<LibrariesDTO> {
            override fun onResponse(call: Call<LibrariesDTO>, response: Response<LibrariesDTO>) {
                if (response.isSuccessful) {
                    val librariesDTO: LibrariesDTO? = response.body()
                    for (library: Library in librariesDTO?.libraries ?: mutableListOf()) {
                        library.name = library.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase() else it.toString()
                        }
                    }
                    libraries.value = librariesDTO?.libraries?.toMutableList()
                }
            }

            override fun onFailure(call: Call<LibrariesDTO>, t: Throwable): Unit {
                libraries.value = mutableListOf()
            }
        })
    }

}
