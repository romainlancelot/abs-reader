package com.absreader.ui.audio_book_library

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.absreader.R
import com.absreader.data.network.dto.audio_book_libraries.Library
import com.absreader.ui.audio_book_library_book.AudioBookLibraryBookActivity
import com.google.android.material.button.MaterialButton

class AudioBookLibraryViewHolder(itemView: View) : ViewHolder(itemView) {
    private val libraryButton: MaterialButton = itemView.findViewById(R.id.libraryButton)

    fun bind(library: Library): Unit {
        libraryButton.text = library.name
        libraryButton.setOnClickListener {
            val intent = Intent(itemView.context, AudioBookLibraryBookActivity::class.java)
            intent.putExtra("libraryId", library.id)
            intent.putExtra("libraryName", "\uD83D\uDCDA ${library.name}")
            itemView.context.startActivity(intent)
        }
    }

}
