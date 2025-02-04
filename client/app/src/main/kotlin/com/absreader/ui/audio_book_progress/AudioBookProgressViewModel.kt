package com.absreader.ui.audio_book_progress

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.absreader.data.network.AudioBookRetrofitClient
import com.absreader.data.network.dto.audio_book_progress.LibraryItem
import com.absreader.data.network.dto.audio_book_progress.ProgressDTO
import com.absreader.data.network.service.AudioBookUserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AudioBookProgressViewModel {

    val progress: MutableLiveData<MutableList<LibraryItem>> = MutableLiveData()

    fun getProgress(context: Context) {
        val client: Retrofit = AudioBookRetrofitClient.getInstance(context)
        val call: Call<ProgressDTO> =
            client.create(AudioBookUserService::class.java).getItemsInProgress()

        call.enqueue(object : Callback<ProgressDTO> {
            override fun onResponse(
                call: Call<ProgressDTO>,
                response: Response<ProgressDTO>
            ): Unit {
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

            override fun onFailure(call: Call<ProgressDTO>, t: Throwable): Unit {
                progress.value = mutableListOf()
            }
        })
    }

}
