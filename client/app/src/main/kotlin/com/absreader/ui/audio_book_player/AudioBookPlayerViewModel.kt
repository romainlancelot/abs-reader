package com.absreader.ui.audio_book_player

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.absreader.data.network.AudioBookRetrofitClient
import com.absreader.data.network.dto.audio_book_item_play.AudioTrack
import com.absreader.data.network.dto.audio_book_item_play.DeviceInfoParameters
import com.absreader.data.network.dto.audio_book_item_play.ItemPlayDTO
import com.absreader.data.network.service.AudioBookItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AudioBookPlayerViewModel : ViewModel() {
    val book: MutableLiveData<ItemPlayDTO> = MutableLiveData()
    val audioFiles: MutableLiveData<List<AudioTrack>> = MutableLiveData()

    fun fetchBook(context: Context, itemId: String) {
        val client: Retrofit = AudioBookRetrofitClient.getInstance(context)
        val deviceInfo = DeviceInfoParameters(
            deviceId = android.os.Build.DEVICE,
            clientName = android.os.Build.MODEL,
            clientVersion = android.os.Build.VERSION.RELEASE,
            manufacturer = android.os.Build.MANUFACTURER,
            model = android.os.Build.MODEL,
            sdkVersion = android.os.Build.VERSION.SDK_INT
        )
        val call: Call<ItemPlayDTO> = client.create(AudioBookItemService::class.java).getItem(itemId, deviceInfo)
        call.enqueue(object : Callback<ItemPlayDTO> {
            override fun onResponse(call: Call<ItemPlayDTO>, response: Response<ItemPlayDTO>) {
                if (response.isSuccessful) {
                    val itemPlayDTO: ItemPlayDTO? = response.body()
                    Toast.makeText(context, itemPlayDTO!!.audioTracks.toString(), Toast.LENGTH_SHORT).show()
                    book.value = itemPlayDTO!!
                    println(itemPlayDTO)
                    audioFiles.value = itemPlayDTO.audioTracks
                } else {
                    Toast.makeText(context, "Failed to fetch book", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ItemPlayDTO>, t: Throwable) {
                Toast.makeText(context, "Failed to fetch audio uri", Toast.LENGTH_SHORT).show()
            }
        })
    }
}