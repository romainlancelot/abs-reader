package com.absreader.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.absreader.networks.dto.libraries.LibrariesDTO
import com.absreader.networks.dto.libraries.Library
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibraryViewModel : ViewModel() {
    val librariesList: MutableLiveData<MutableList<Library>> =
        MutableLiveData<MutableList<Library>>()

    fun getLibraries(call: Call<LibrariesDTO>) {
        call.enqueue(object : Callback<LibrariesDTO> {
            override fun onResponse(call: Call<LibrariesDTO>, response: Response<LibrariesDTO>) {
                if (response.isSuccessful) {
                    val librariesDTO: LibrariesDTO? = response.body()
                    librariesList.value = librariesDTO?.libraries?.toMutableList()
                    return
                }
            }

            override fun onFailure(call: Call<LibrariesDTO>, t: Throwable) {
                librariesList.value = mutableListOf()
            }
        })
    }
}