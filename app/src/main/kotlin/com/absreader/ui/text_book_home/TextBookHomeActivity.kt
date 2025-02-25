package com.absreader.ui.text_book_home

import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.absreader.R
import com.absreader.utils.NavigationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.absreader.data.repository.TextBookBookRepository

class TextBookHomeActivity : AppCompatActivity() {

    private lateinit var viewModel: TextBookHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_book_home)

        val repository = TextBookBookRepository(application)
        val factory = TextBookHomeViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, factory)
                .get(TextBookHomeViewModel::class.java)

        val buttonLogout: Button = findViewById(R.id.buttonLogout)
        val recyclerViewAllBooks: RecyclerView = findViewById(R.id.recyclerViewAllBooks)
        val recyclerViewMyBooks: RecyclerView = findViewById(R.id.recyclerViewMyBooks)
        val recyclerViewMyReadings: RecyclerView = findViewById(R.id.recyclerViewMyReadings)

        recyclerViewAllBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewMyBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewMyReadings.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        viewModel.books.observe(this) { books ->
            recyclerViewAllBooks.adapter = TextBookHomeBookAdapter(books)
        }

        viewModel.myBooks.observe(this) { books ->
            recyclerViewMyBooks.adapter = TextBookHomeBookAdapter(books)
        }

        viewModel.bookmarkedBooks.observe(this) { books ->
            recyclerViewMyReadings.adapter = TextBookHomeBookAdapter(books)
        }

        buttonLogout.setOnClickListener {
            NavigationUtils.logOutFromTextBook(this)
        }

        viewModel.fetchAllBooks()
    }
}
