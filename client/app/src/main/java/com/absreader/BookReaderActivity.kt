package com.absreader;

import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.enableEdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.absreader.utils.HeaderManager;

class BookReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge();
        setContentView(R.layout.activity_book_reader);

//        HeaderManager(findViewById(R.id.header)).setup(getString(R.string.reader));

        val bookId: String = intent.getStringExtra("bookId").toString();
        val bookTitle: String = intent.getStringExtra("bookTitle").toString();

        findViewById<TextView>(R.id.bookTitle).text = bookTitle;

        fetchBookContent(bookId);
    }

    private fun fetchBookContent(bookId: String) {
        val contentTextView = findViewById<TextView>(R.id.bookContent);
        contentTextView.text = "Loading...";
    }

}
