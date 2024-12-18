package com.absreader.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.absreader.R
import com.absreader.networks.dto.progress.LibraryItem
import com.squareup.picasso.Picasso

class ProgressViewHolder(itemView: View) : ViewHolder(itemView) {
    private val cover: ImageView = itemView.findViewById(R.id.cover)
    private val title: TextView = itemView.findViewById(R.id.title)

    fun bind(libraryBook: LibraryItem) {
        if (libraryBook.media.coverPath != null) {
            Picasso.get().load(libraryBook.media.coverPath).into(cover)
        } else {
            cover.setImageResource(R.drawable.generic_cover)
        }
        title.text = libraryBook.media.metadata.title
    }
}