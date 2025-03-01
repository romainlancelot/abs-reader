package com.absreader.ui.audio_book_progress

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.absreader.R
import com.absreader.data.network.dto.audio_book_progress.LibraryItem
import com.absreader.ui.audio_book_selected_text_book_menu.AudioBookSelectedTextBookMenuActivity
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class AudioBookProgressViewHolder(itemView: View) : ViewHolder(itemView) {
    private val cover: ImageView = itemView.findViewById(R.id.cover)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val bookButton: MaterialCardView = itemView.findViewById(R.id.card)

    fun bind(libraryBook: LibraryItem) {
        if (libraryBook.media.coverPath != null) {
            Picasso.get().load(libraryBook.media.coverPath).into(cover)
        } else {
            cover.setImageResource(R.drawable.generic_cover)
        }
        if (libraryBook.media.metadata.title.length > 30) {
            title.text = libraryBook.media.metadata.title.substring(0, 30) + "..."
        } else {
            title.text = libraryBook.media.metadata.title
        }
        bookButton.setOnClickListener {
            val intent: Intent =
                Intent(itemView.context, AudioBookSelectedTextBookMenuActivity::class.java)
            intent.putExtra("coverPath", libraryBook.media.coverPath)
            intent.putExtra("title", "\uD83D\uDCD6 " + libraryBook.media.metadata.title)
            intent.putExtra("itemId", libraryBook.id)
            itemView.context.startActivity(intent)
        }
    }
}
