package com.absreader

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.adapters.LibraryBooksAdapter
import com.absreader.networks.dto.library_items.Result
import com.absreader.utils.HeaderManager
import com.absreader.view_models.LibraryBooksViewModel

class LibraryBooksActivity : AppCompatActivity() {
    private val viewModel: LibraryBooksViewModel = LibraryBooksViewModel()

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
            recyclerView.adapter = LibraryBooksAdapter(libraryItems)
            if (libraryItems.isEmpty()) {
                noBooksTextView.visibility = TextView.VISIBLE
            }
        }
        this.viewModel.getBooks(this@LibraryBooksActivity, libraryId)

    }
}