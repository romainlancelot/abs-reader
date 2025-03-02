package com.absreader.ui.audio_book_library_book

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.data.network.dto.audio_book_library_items.Result
import com.absreader.ui.audio_book_upload.AudioBookUploadActivity
import com.absreader.utils.HeaderManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AudioBookLibraryBookActivity : AppCompatActivity() {
    private val viewModel: AudioBookLibraryBookViewModel = AudioBookLibraryBookViewModel()
    private lateinit var searchView: SearchView
    private lateinit var adapter: AudioBookLibraryBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_library)
        val libraryName: String = intent.getStringExtra("libraryName").toString()
        HeaderManager(findViewById(R.id.header)).setup(libraryName)
        val noBooksTextView: TextView = findViewById<TextView>(R.id.noBooks)
        val libraryId: String = intent.getStringExtra("libraryId").toString()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val uploadButton = findViewById<FloatingActionButton>(R.id.uploadButton)
        uploadButton.setOnClickListener {
            val intent = Intent(this, AudioBookUploadActivity::class.java).apply {
                putExtra("libraryId", libraryId)
                putExtra("libraryFolders", intent.getStringExtra("libraryFolders"))
            }
            startActivity(intent)
        }

        this.viewModel.books.observe(this) { libraryItems: List<Result> ->
            adapter = AudioBookLibraryBookAdapter(libraryItems)
            recyclerView.adapter = adapter
            if (libraryItems.isEmpty()) {
                noBooksTextView.visibility = TextView.VISIBLE
            }
        }
        this.viewModel.getBooks(this@AudioBookLibraryBookActivity, libraryId)
        searchView = findViewById(R.id.search)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText ?: "")
                return true
            }
        })
        refreshApp()
    }

    private fun refreshApp() {
        val swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            this.viewModel.getBooks(this@AudioBookLibraryBookActivity, intent.getStringExtra("libraryId").toString())
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
