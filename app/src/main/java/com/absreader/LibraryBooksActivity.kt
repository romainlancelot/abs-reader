package com.absreader

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.adapters.LibraryBooksAdapter
import com.absreader.networks.dto.library_items.Result
import com.absreader.view_models.LibraryBooksViewModel

class LibraryBooksActivity : AppCompatActivity() {
    private val viewModel: LibraryBooksViewModel = LibraryBooksViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library)
        val libraryTextView: TextView = findViewById<TextView>(R.id.libraryName)
        val libraryId: String = intent.getStringExtra("libraryId").toString()
        libraryTextView.text = intent.getStringExtra("libraryName").toString()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        this.viewModel.books.observe(this) { libraryItems: List<Result> ->
            recyclerView.adapter = LibraryBooksAdapter(libraryItems)
        }
        this.viewModel.getBooks(this@LibraryBooksActivity, libraryId)

    }
}