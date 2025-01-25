package com.absreader.ui.audio_book_library_book

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.data.network.dto.audio_book_library_items.Result
import com.absreader.utils.HeaderManager

class AudioBookLibraryBookActivity : AppCompatActivity() {
    private val viewModel: AudioBookLibraryBookViewModel = AudioBookLibraryBookViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library)
        HeaderManager(findViewById(R.id.header)).setup(
            intent.getStringExtra("libraryName").toString()
        )
        val noBooksTextView: TextView = findViewById<TextView>(R.id.noBooks)
        val libraryId: String = intent.getStringExtra("libraryId").toString()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        this.viewModel.books.observe(this) { libraryItems: List<Result> ->
            recyclerView.adapter = AudioBookLibraryBookAdapter(libraryItems)
            if (libraryItems.isEmpty()) {
                noBooksTextView.visibility = TextView.VISIBLE
            }
        }
        this.viewModel.getBooks(this@AudioBookLibraryBookActivity, libraryId)

    }
}
