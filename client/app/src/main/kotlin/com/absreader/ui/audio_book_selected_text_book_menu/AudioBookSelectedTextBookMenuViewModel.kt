package com.absreader.ui.audio_book_selected_text_book_menu

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.absreader.data.network.AudioBookRetrofitClient
import com.absreader.data.network.dto.audio_book_item_play.ItemPlayDTO
import com.absreader.data.network.service.AudioBookItemService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit

class AudioBookSelectedTextBookMenuViewModel: ViewModel() {
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
}