package com.absreader.ui.audio_book_selected_text_book_menu

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.data.network.dto.library_items_data.LibraryFile
import com.absreader.ui.audio_book_player.AudioBookPlayerActivity
import com.folioreader.FolioReader
import java.io.File

class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val bookName: TextView = itemView.findViewById(R.id.bookName)
    private val readButton: Button = itemView.findViewById(R.id.readButton)
    private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

    fun bind(libraryFile: LibraryFile, itemId: String) {
        val computeBookName: String = libraryFile.metadata.relPath.take(40) + "..."
        bookName.text = libraryFile.metadata.relPath.take(20) + "..."
        val file: File = File(itemView.context.filesDir, libraryFile.metadata.filename)
        if (file.exists()) {
            bookName.text = "\uD83D\uDCBE " + computeBookName
        } else {
            bookName.text = "❌ " + computeBookName
        }
        val viewModel: AudioBookSelectedTextBookMenuViewModel =
            AudioBookSelectedTextBookMenuViewModel()
        readButton.setOnClickListener {
            itemView.findViewTreeLifecycleOwner()?.let { it1 ->
                viewModel.book.observe(it1) { book ->
                    if (book && libraryFile.fileType == "audio") {
                        val intent = Intent(itemView.context, AudioBookPlayerActivity::class.java)
                        val activity = itemView.context as? AudioBookSelectedTextBookMenuActivity
                        val coverPath = activity?.intent?.getStringExtra("coverPath")
                        val title = activity?.intent?.getStringExtra("title")
                        intent.putExtra("bookName", title ?: libraryFile.metadata.relPath)
                        intent.putExtra("coverPath", coverPath)
                        intent.putExtra("itemId", itemId)
                        intent.putExtra("absPath", file.absolutePath)
                        itemView.context.startActivity(intent)
                    } else {
                        val reader: FolioReader = FolioReader.get()
                        reader.openBook(file.absolutePath)
                    }
                }
                if (file.exists()) {
                    viewModel.book.value = true
                } else {
                    val text: String =
                        if (libraryFile.fileType == "audio") "Downloading audio book" else "Downloading epub book"
                    Toast.makeText(itemView.context, text, Toast.LENGTH_SHORT)
                        .show()
                    viewModel.downloadBook(itemView.context, libraryFile.metadata.path)
                }
            }
        }
        deleteButton.setOnClickListener {
            viewModel.deleteBook(itemView.context, itemId, libraryFile.ino)
            val activity = itemView.context as? AudioBookSelectedTextBookMenuActivity
            activity?.recreate()
        }
    }
}
