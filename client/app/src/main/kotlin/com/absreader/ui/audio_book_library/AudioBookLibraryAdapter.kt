package com.absreader.ui.audio_book_library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.absreader.R
import com.absreader.data.network.dto.audio_book_libraries.Library

class AudioBookLibraryAdapter(private val libraries: List<Library>) : Adapter<AudioBookLibraryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBookLibraryViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.library_item, parent, false);
        return AudioBookLibraryViewHolder(view);
    }

    override fun onBindViewHolder(holder: AudioBookLibraryViewHolder, position: Int): Unit {
        holder.bind(libraries[position]);
    }

    override fun getItemCount(): Int {
        return libraries.size
    }

}
