package com.absreader.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.networks.dto.libraries.Library

class HomeActivity : AppCompatActivity() {
    private val viewModel: LibraryViewModel = LibraryViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        this.viewModel.libraries.observe(this) { libraries: List<Library> ->
            recyclerView.adapter = LibraryAdapter(libraries)
        }
        this.viewModel.getLibraries(this@HomeActivity)
    }
}