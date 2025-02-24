package com.absreader.ui.audio_book_progress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.absreader.R
import com.absreader.data.network.dto.audio_book_progress.LibraryItem

class AudioBookProgressAdapter(private val progress: List<LibraryItem>) : Adapter<AudioBookProgressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBookProgressViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.library_book, parent, false)
        return AudioBookProgressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioBookProgressViewHolder, position: Int) {
        holder.bind(progress[position])
    }

    override fun getItemCount(): Int {
        return progress.size
    }
}