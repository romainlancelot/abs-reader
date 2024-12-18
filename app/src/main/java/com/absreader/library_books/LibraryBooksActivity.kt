package com.absreader.library_books

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.networks.dto.library_items.Result

class LibraryBooksActivity : AppCompatActivity() {
    private val viewModel: LibraryBooksViewModel = LibraryBooksViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library)
        val server: String = getSharedPreferences("absreader", MODE_PRIVATE)
            .getString("server", "").toString()
        val libraryId: String = intent.getStringExtra("libraryId").toString()
        val libraryTextView: TextView = findViewById<TextView>(R.id.libraryName)
        libraryTextView.text = intent.getStringExtra("libraryName").toString()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        this.viewModel.books.observe(this) { libraryItems: List<Result> ->
            for (libraryItem: Result in libraryItems) {
                libraryItem.media.coverPath = server + "/api/items/${libraryItem.id}/cover"
            }
            recyclerView.adapter = LibraryBooksAdapter(libraryItems)
        }
        this.viewModel.getBooks(this@LibraryBooksActivity, libraryId)

    }
}