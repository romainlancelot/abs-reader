package com.absreader

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.adapters.LibraryAdapter
import com.absreader.adapters.ProgressAdapter
import com.absreader.networks.dto.libraries.Library
import com.absreader.networks.dto.progress.LibraryItem
import com.absreader.utils.HeaderManager
import com.absreader.view_models.LibraryViewModel
import com.absreader.view_models.ProgressViewModel

class HomeActivity : AppCompatActivity() {
    private val libraryViewModel: LibraryViewModel = LibraryViewModel()
    private val progressViewModel: ProgressViewModel = ProgressViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        val headerManager: HeaderManager = HeaderManager(findViewById(R.id.header), false)
        headerManager.setup(getString(R.string.home))
        val noLibraryText: TextView = findViewById(R.id.noLibraries)
        val noProgressText: TextView = findViewById(R.id.noProgress)
        val libraryRecyclerView: RecyclerView = findViewById(R.id.libraryRecyclerView)
        libraryRecyclerView.layoutManager = GridLayoutManager(this, 1)
        this.libraryViewModel.libraries.observe(this) { libraries: List<Library> ->
            libraryRecyclerView.adapter = LibraryAdapter(libraries)
            if (libraries.isEmpty()) {
                noLibraryText.visibility = TextView.VISIBLE
            }
        }
        this.libraryViewModel.getLibraries(this@HomeActivity)
        val progressRecyclerView: RecyclerView = findViewById(R.id.progressRecyclerView)
        progressRecyclerView.layoutManager = GridLayoutManager(this, 2)
        this.progressViewModel.progress.observe(this) { progress: List<LibraryItem> ->
            progressRecyclerView.adapter = ProgressAdapter(progress)
            if (progress.isEmpty()) {
                noProgressText.visibility = TextView.VISIBLE
            }
        }
        this.progressViewModel.getProgress(this@HomeActivity)
    }
}