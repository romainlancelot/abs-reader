package com.absreader.ui.audio_book_selected_text_book_menu

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.absreader.R
import com.absreader.data.network.AudioBookRetrofitClient
import com.absreader.data.network.dto.library_items_data.LibraryItemDTO
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
    val bookData: MutableLiveData<LibraryItemDTO> = MutableLiveData()

    fun deleteBook(context: Context, itemId: String, ino: String) {
        val client: Retrofit = AudioBookRetrofitClient.getInstance(context)
        val call: Call<Void> =
            client.create(AudioBookItemService::class.java).deleteItem(itemId, ino)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, context.getString(R.string.book_deleted), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.failed_to_delete_book), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.failed_to_delete_book), Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, context.getString(R.string.failed_to_download_book), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.failed_to_get_book_data), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getItem(context: Context, itemId: String) {
        val client: Retrofit = AudioBookRetrofitClient.getInstance(context)
        val call: Call<LibraryItemDTO> =
            client.create(AudioBookItemService::class.java).getItem(itemId)
        call.enqueue(object : Callback<LibraryItemDTO> {
            override fun onResponse(
                call: Call<LibraryItemDTO>,
                response: Response<LibraryItemDTO>
            ) {
                if (response.isSuccessful) {
                    bookData.value = response.body()
                }
            }

            override fun onFailure(p0: Call<LibraryItemDTO>, p1: Throwable) {
                Toast.makeText(
                    context,
                    "Failed to get book data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
