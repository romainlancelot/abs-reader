package com.absreader.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.absreader.R
import com.absreader.networks.dto.libraries.Library


class LibraryAdapter(private val libraries: List<Library>) : Adapter<LibraryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.library_item, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bind(libraries[position])
    }

    override fun getItemCount(): Int {
        return libraries.size
    }
}