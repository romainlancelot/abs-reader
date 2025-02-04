package com.absreader.ui.text_book_book_details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.absreader.R
import com.absreader.data.repository.TextBookBookRepository
import com.absreader.ui.text_book_reading.TextBookReadingActivity
import com.absreader.utils.NavigationUtils
import com.squareup.picasso.Picasso

class TextBookBookDetailsActivity() : AppCompatActivity() {

    private lateinit var viewModel: TextBookBookDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_book_book_details)

        if (!NavigationUtils.hasTextBookApiJwt(this)) {
            NavigationUtils.redirectToTextBookLogInView(this)
            return
        }

        val repository = TextBookBookRepository(application)
        val factory = TextBookBookDetailsViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, factory)
                .get(TextBookBookDetailsViewModel::class.java)

        val bookId = intent.getStringExtra("BOOK_ID")
        if (bookId == null) {
            finish()
            return
        }

        val imageViewCover = findViewById<ImageView>(R.id.imageViewCover)
        val textViewTitle = findViewById<TextView>(R.id.textViewTitle)
        val textViewAuthor = findViewById<TextView>(R.id.textViewAuthor)
        val textViewYear = findViewById<TextView>(R.id.textViewYear)
        val buttonEdit = findViewById<Button>(R.id.buttonEdit)
        val buttonRead = findViewById<Button>(R.id.buttonRead)
        val buttonGoToHomeView = findViewById<Button>(R.id.buttonGoToHomeView)

        viewModel.bookDetails.observe(this) { details ->
            textViewTitle.text = details.title
            textViewAuthor.text = "Author: ${details.authorName}"
            textViewYear.text = "Year: ${details.createdYear}"

            Picasso.get()
                .load(details.coverUrl)
                .into(imageViewCover)

            buttonEdit.visibility = if (details.isTheReaderTheAuthor) View.VISIBLE else View.GONE
        }

        buttonEdit.setOnClickListener {
        }

        buttonRead.setOnClickListener {
            val intent = Intent(this, TextBookReadingActivity::class.java)
            intent.putExtra("BOOK_ID", bookId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        buttonGoToHomeView.setOnClickListener {
            NavigationUtils.redirectToTextBookHomeView(this)
        }

        viewModel.fetchBookDetails(bookId)
    }

}
