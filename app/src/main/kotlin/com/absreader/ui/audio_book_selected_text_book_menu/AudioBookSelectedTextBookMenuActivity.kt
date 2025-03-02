package com.absreader.ui.audio_book_selected_text_book_menu

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.utils.HeaderManager
import com.squareup.picasso.Picasso

class AudioBookSelectedTextBookMenuActivity : AppCompatActivity() {
    private val viewModel: AudioBookSelectedTextBookMenuViewModel =
        AudioBookSelectedTextBookMenuViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_book_selected_text_book_menu)
        HeaderManager(findViewById(R.id.header)).setup(getString(R.string.details))
        val coverPath: String? = intent.getStringExtra("coverPath")
        val title: String = intent.getStringExtra("title").toString()
        val noBooksTextView: TextView = findViewById(R.id.noBooks)
        findViewById<TextView>(R.id.bookTitle).text = title
        if (coverPath != null) {
            Picasso.get().load(coverPath).into(findViewById<ImageView>(R.id.cover))
        } else {
            findViewById<ImageView>(R.id.cover).setImageResource(R.drawable.generic_cover)
        }
        val itemId = intent.getStringExtra("itemId")
        val recyclerView: RecyclerView = findViewById(R.id.bookRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        this.viewModel.bookData.observe(this) { libraryItemDTO ->
            recyclerView.adapter = itemId?.let {
                BookAdapter(libraryItemDTO.libraryFiles, it)
            }
            if (libraryItemDTO.libraryFiles.isEmpty()) {
                noBooksTextView.visibility = TextView.VISIBLE
            }
        }
        itemId?.let { this.viewModel.getItem(this, it) }
        val deleteWholeButton: Button = findViewById(R.id.deleteWholeButton)
        deleteWholeButton.setOnClickListener {
            itemId?.let { it1 ->
                this.viewModel.deleteSerie(this, it1)
                finish()
            }
        }
    }

    fun refreshApp() {
        val swipeRefreshLayout: androidx.swiperefreshlayout.widget.SwipeRefreshLayout =
            findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            this.viewModel.getItem(this, intent.getStringExtra("itemId").toString())
            swipeRefreshLayout.isRefreshing = false
        }
    }
}