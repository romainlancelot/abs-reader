package com.absreader.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.networks.dto.libraries.Library
import com.google.android.material.button.MaterialButton

class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val libraryButton: MaterialButton = itemView.findViewById(R.id.libraryButton)

    fun bind(library: Library) {
        libraryButton.text = library.name
    }
}