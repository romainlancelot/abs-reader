package com.absreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.absreader.R
import com.absreader.networks.dto.progress.LibraryItem
import com.absreader.view_holders.ProgressViewHolder

class ProgressAdapter(private val progress: List<LibraryItem>) : Adapter<ProgressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.library_book, parent, false)
        return ProgressViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        holder.bind(progress[position])
    }

    override fun getItemCount(): Int {
        return progress.size
    }
}