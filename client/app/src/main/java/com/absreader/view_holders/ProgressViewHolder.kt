package com.absreader.view_holders

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.absreader.BookActivity
import com.absreader.R
import com.absreader.networks.dto.progress.LibraryItem
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class ProgressViewHolder(itemView: View) : ViewHolder(itemView) {
    private val cover: ImageView = itemView.findViewById(R.id.cover)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val bookButton: MaterialCardView = itemView.findViewById(R.id.card)

    fun bind(libraryBook: LibraryItem) {
        if (libraryBook.media.coverPath != null) {
            Picasso.get().load(libraryBook.media.coverPath).into(cover)
        } else {
            cover.setImageResource(R.drawable.generic_cover)
        }
        title.text = libraryBook.media.metadata.title
        bookButton.setOnClickListener {
            val intent: Intent = Intent(itemView.context, BookActivity::class.java)
            intent.putExtra("coverPath", libraryBook.media.coverPath)
            intent.putExtra("title", "\uD83D\uDCD6 " + libraryBook.media.metadata.title)
            if (libraryBook.media.metadata.authorName == null) {
                intent.putExtra("author", "No author available.")
            } else {
                intent.putExtra("author", libraryBook.media.metadata.authorName)
            }
            if (libraryBook.media.metadata.description == null) {
                intent.putExtra("description", "📝 No description available.")
            } else {
                intent.putExtra("description", "📝 " + libraryBook.media.metadata.description)
            }
            itemView.context.startActivity(intent)
        }
    }
}