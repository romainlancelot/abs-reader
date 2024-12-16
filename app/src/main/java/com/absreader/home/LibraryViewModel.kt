package com.absreader.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.absreader.networks.RetrofitFactory
import com.absreader.networks.dto.libraries.LibrariesDTO
import com.absreader.networks.dto.libraries.Library
import com.absreader.networks.services.HomeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LibraryViewModel : ViewModel() {
    val libraries: MutableLiveData<MutableList<Library>> = MutableLiveData<MutableList<Library>>()

    fun getLibraries(server: String, bearer: String) {
        val client: Retrofit = RetrofitFactory.getInstance(server)
        val call: Call<LibrariesDTO> = client.create(HomeService::class.java).getLibrairies(bearer)
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