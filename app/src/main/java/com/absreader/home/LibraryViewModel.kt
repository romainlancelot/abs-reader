package com.absreader.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.absreader.networks.RetrofitFactory
import com.absreader.networks.dto.libraries.LibrariesDTO
import com.absreader.networks.dto.libraries.Library
import com.absreader.networks.services.LibraryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LibraryViewModel : ViewModel() {
    val libraries: MutableLiveData<MutableList<Library>> = MutableLiveData<MutableList<Library>>()

    fun getLibraries(context: Context) {
        val client: Retrofit = RetrofitFactory.getInstance(context)
        val call: Call<LibrariesDTO> = client.create(LibraryService::class.java).getLibraries()
        call.enqueue(object : Callback<LibrariesDTO> {
            override fun onResponse(call: Call<LibrariesDTO>, response: Response<LibrariesDTO>) {
                if (response.isSuccessful) {
                    val librariesDTO: LibrariesDTO? = response.body()
                    libraries.value = librariesDTO?.libraries?.toMutableList()
                }
            }

            override fun onFailure(call: Call<LibrariesDTO>, t: Throwable) {
                libraries.value = mutableListOf()
            }
        })
    }
}