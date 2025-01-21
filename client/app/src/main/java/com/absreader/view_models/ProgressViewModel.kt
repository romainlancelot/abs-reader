package com.absreader.view_models

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.absreader.networks.RetrofitFactory
import com.absreader.networks.dto.progress.LibraryItem
import com.absreader.networks.dto.progress.ProgressDTO
import com.absreader.networks.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ProgressViewModel {
    val progress: MutableLiveData<MutableList<LibraryItem>> = MutableLiveData()

    fun getProgress(context: Context) {
        val client: Retrofit = RetrofitFactory.getInstance(context)
        val call: Call<ProgressDTO> = client.create(UserService::class.java).getItemsInProgress()
        call.enqueue(object : Callback<ProgressDTO> {
            override fun onResponse(call: Call<ProgressDTO>, response: Response<ProgressDTO>) {
                if (response.isSuccessful) {
                    val progressDTO: ProgressDTO? = response.body()
                    for (libraryItem: LibraryItem in progressDTO?.libraryItems ?: mutableListOf()) {
                        if (libraryItem.media.coverPath == null) {
                            continue
                        }
                        libraryItem.media.coverPath =
                            client.baseUrl().toString() + "/api/items/${libraryItem.id}/cover"
                    }
                    progress.value = progressDTO?.libraryItems?.toMutableList()
                }
            }

            override fun onFailure(call: Call<ProgressDTO>, t: Throwable) {
                progress.value = mutableListOf()
            }
        })
    }
}