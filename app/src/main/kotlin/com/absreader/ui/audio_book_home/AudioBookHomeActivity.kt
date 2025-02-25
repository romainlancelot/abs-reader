package com.absreader.ui.audio_book_home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.ui.audio_book_library.AudioBookLibraryAdapter
import com.absreader.ui.audio_book_progress.AudioBookProgressAdapter
import com.absreader.data.network.dto.audio_book_libraries.Library
import com.absreader.data.network.dto.audio_book_progress.LibraryItem
import com.absreader.utils.HeaderManager
import com.absreader.ui.audio_book_library.AudioBookLibraryViewModel
import com.absreader.ui.audio_book_progress.AudioBookProgressViewModel
import com.absreader.ui.base.BaseActivity

class AudioBookHomeActivity : BaseActivity() {

    private val audioBookLibraryViewModel: AudioBookLibraryViewModel = AudioBookLibraryViewModel()
    private val audioBookProgressViewModel: AudioBookProgressViewModel = AudioBookProgressViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = findViewById<FrameLayout>(R.id.container)
        layoutInflater.inflate(R.layout.activity_audio_book_home, container, true)
        enableEdgeToEdge()
        HeaderManager(findViewById(R.id.header), false).setup(getString(R.string.home))
        val noLibraryText: TextView = findViewById(R.id.noLibraries)
        val noProgressText: TextView = findViewById(R.id.noProgress)
        val libraryRecyclerView: RecyclerView = findViewById(R.id.libraryRecyclerView)
        libraryRecyclerView.layoutManager = GridLayoutManager(this, 1)
        this.audioBookLibraryViewModel.libraries.observe(this) { libraries: List<Library> ->
            libraryRecyclerView.adapter = AudioBookLibraryAdapter(libraries)
            if (libraries.isEmpty()) {
                noLibraryText.visibility = TextView.VISIBLE
            }
        }
        this.audioBookLibraryViewModel.getLibraries(this@AudioBookHomeActivity)
        val progressRecyclerView: RecyclerView = findViewById(R.id.progressRecyclerView)
        progressRecyclerView.layoutManager = GridLayoutManager(this, 2)
        this.audioBookProgressViewModel.progress.observe(this) { progress: List<LibraryItem> ->
            progressRecyclerView.adapter = AudioBookProgressAdapter(progress)
            if (progress.isEmpty()) {
                noProgressText.visibility = TextView.VISIBLE
            }
        }
        this.audioBookProgressViewModel.getProgress(this@AudioBookHomeActivity)
    }

}
