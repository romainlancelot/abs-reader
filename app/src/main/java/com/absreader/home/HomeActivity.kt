package com.absreader.home

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.networks.RetrofitFactory
import com.absreader.networks.dto.libraries.LibrariesDTO
import com.absreader.networks.dto.libraries.Library
import com.absreader.networks.services.HomeService
import retrofit2.Call
import retrofit2.Retrofit

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("com.absreader", MODE_PRIVATE)
        val server: String = sharedPreferences.getString("server", "").toString()
        val bearer: String = sharedPreferences.getString("bearer", "").toString()
        val client: Retrofit = RetrofitFactory.getInstance(server)
        val call: Call<LibrariesDTO> = client.create(HomeService::class.java).getLibrairies(bearer)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        val viewModel: LibraryViewModel = LibraryViewModel()
        viewModel.librariesList.observe(this, { libraries: List<Library> ->
            recyclerView.adapter = LibraryAdapter(libraries)
        })
        viewModel.getLibraries(call)
    }
}