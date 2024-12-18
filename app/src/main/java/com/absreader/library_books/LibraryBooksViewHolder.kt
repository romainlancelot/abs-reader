package com.absreader.library_books

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.absreader.R
import com.absreader.networks.dto.library_items.Result
import com.squareup.picasso.Picasso

class LibraryBooksViewHolder(itemView: View) : ViewHolder(itemView) {
    private val cover: ImageView = itemView.findViewById(R.id.cover)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val author: TextView = itemView.findViewById(R.id.author)

    fun bind(libraryBook: Result) {
        println(libraryBook.media.coverPath)
        Picasso.get().load(libraryBook.media.coverPath).into(cover)
        title.text = libraryBook.media.metadata.title
        author.text = libraryBook.media.metadata.authorName
    }
}