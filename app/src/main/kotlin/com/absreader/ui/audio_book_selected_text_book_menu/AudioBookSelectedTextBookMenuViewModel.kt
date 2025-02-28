package com.absreader.ui.audio_book_selected_text_book_menu

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.absreader.data.network.AudioBookRetrofitClient
import com.absreader.data.network.dto.audio_book_item_play.ItemPlayDTO
import com.absreader.data.network.service.AudioBookItemService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream

class AudioBookSelectedTextBookMenuViewModel : ViewModel() {
    val book: MutableLiveData<Boolean> = MutableLiveData()

    fun deleteBook(context: Context, itemId: String) {
        val client: Retrofit = AudioBookRetrofitClient.getInstance(context)
        val call: Call<Void> = client.create(AudioBookItemService::class.java).deleteItem(itemId)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Book deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to delete book", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Failed to delete book", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun downloadBook(context: Context, fileName: String) {
        val client: Retrofit = AudioBookRetrofitClient.getInstance(context)
        val call: Call<ResponseBody> =
            client.create(AudioBookItemService::class.java).downloadFile(fileName)
        val fileNameOnly = fileName.substring(fileName.lastIndexOf("/") + 1)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val stream = response.body()?.byteStream()
                    if (stream != null) {
                        val output = File(context.filesDir, fileNameOnly)
                        val outputStream = FileOutputStream(output)
                        val buffer = ByteArray(1024)
                        var read: Int
                        while (stream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }
                        outputStream.flush()
                        outputStream.close()
                        stream.close()
                        book.value = true
                    }
                } else {
                    Toast.makeText(context, "Failed to download book", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Failed to download book", Toast.LENGTH_SHORT).show()
            }
        })
    }

}

private fun <T> Call<T>.enqueue(callback: Callback<ItemPlayDTO>) {

}
