package com.absreader.ui.text_book_reading

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.absreader.R
import com.absreader.data.repository.TextBookBookRepository
import com.absreader.data.repository.TextBookBookmarkRepository
import com.absreader.ui.text_book_book_details.TextBookBookDetailsActivity
import com.squareup.picasso.Picasso

class TextBookReadingActivity : AppCompatActivity() {

    private lateinit var viewModel: TextBookReadingViewModel
    private lateinit var bookId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_book_reading)

        bookId = intent.getStringExtra("BOOK_ID") ?: run {
            finish()
            return
        }

        val factory = TextBookReadingViewModelFactory(
            application,
            TextBookBookRepository(application),
            TextBookBookmarkRepository(application)
        )
        viewModel =
            ViewModelProvider(this, factory)
                .get(TextBookReadingViewModel::class.java)

        val imageViewPage = findViewById<ImageView>(R.id.imageViewPage)
        val buttonPrevious = findViewById<Button>(R.id.buttonPrevious)
        val buttonNext = findViewById<Button>(R.id.buttonNext)
        val buttonGoToDetails = findViewById<Button>(R.id.buttonGoToDetails)

        viewModel.currentPage.observe(this) { page ->
            Picasso.get()
                .load(page.fileUrl)
                .into(imageViewPage)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        buttonPrevious.setOnClickListener {
            viewModel.previousPage(bookId)
        }

        buttonNext.setOnClickListener {
            viewModel.nextPage(bookId)
        }

        buttonGoToDetails.setOnClickListener {
            val intent = Intent(this, TextBookBookDetailsActivity::class.java)
            intent.putExtra("BOOK_ID", bookId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        viewModel.loadInitialPage(bookId)
        viewModel.fetchBookDetails(bookId)
    }
}
