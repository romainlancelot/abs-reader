package com.absreader.ui.audio_book_library_book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.absreader.R
import com.absreader.data.network.dto.audio_book_library_items.Result

class AudioBookLibraryBookAdapter(private val books: List<Result>) : Adapter<AudioBookLibraryBookViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBookLibraryBookViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.library_book, parent, false)
        return AudioBookLibraryBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioBookLibraryBookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int {
        return books.size
    }

}
