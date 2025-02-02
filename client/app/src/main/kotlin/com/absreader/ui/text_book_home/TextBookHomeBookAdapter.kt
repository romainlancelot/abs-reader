package com.absreader.ui.text_book_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.data.model.text_book.Book
import com.squareup.picasso.Picasso

class TextBookHomeBookAdapter(
    private val books: List<Book>
) : RecyclerView.Adapter<TextBookHomeBookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewCover: ImageView = itemView.findViewById(R.id.imageViewCover)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_book_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.textViewTitle.text = book.title

        Picasso.get()
            .load(book.coverUrl)
            .into(holder.imageViewCover)
    }

    override fun getItemCount(): Int = books.size
}
