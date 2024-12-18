package com.absreader.view_holders

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.absreader.LibraryBooksActivity
import com.absreader.R
import com.absreader.networks.dto.libraries.Library
import com.google.android.material.button.MaterialButton

class LibraryViewHolder(itemView: View) : ViewHolder(itemView) {
    private val libraryButton: MaterialButton = itemView.findViewById(R.id.libraryButton)

    fun bind(library: Library) {
        libraryButton.text = library.name
        libraryButton.setOnClickListener {
            val intent = Intent(itemView.context, LibraryBooksActivity::class.java)
            intent.putExtra("libraryId", library.id)
            intent.putExtra("libraryName", "\uD83D\uDCE6 ${library.name}")
            itemView.context.startActivity(intent)
        }
    }
}