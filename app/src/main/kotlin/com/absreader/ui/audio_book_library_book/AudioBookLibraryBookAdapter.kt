package com.absreader.ui.audio_book_library_book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.absreader.R
import com.absreader.data.network.dto.audio_book_library_items.Result
import java.util.Locale

class AudioBookLibraryBookAdapter(private var books: List<Result>) : Adapter<AudioBookLibraryBookViewHolder>() {
    private var filteredBooks: List<Result> = books

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBookLibraryBookViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.library_book, parent, false)
        return AudioBookLibraryBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioBookLibraryBookViewHolder, position: Int) {
        holder.bind(filteredBooks[position])
    }

    override fun getItemCount(): Int {
        return filteredBooks.size
    }

    fun filter(query: String) {
        filteredBooks = if (query.isEmpty()) {
            books
        } else {
            books.filter {
                it.media.metadata.title.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        }
        notifyDataSetChanged()
    }
}
