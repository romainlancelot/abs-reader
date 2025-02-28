package com.absreader.ui.audio_book_selected_text_book_menu

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.utils.HeaderManager
import com.folioreader.FolioReader
import com.squareup.picasso.Picasso
import java.io.File

class AudioBookSelectedTextBookMenuActivity : AppCompatActivity() {
    private lateinit var readNowButton: Button
    private lateinit var deleteButton: Button
    private val viewModel: AudioBookSelectedTextBookMenuViewModel =
        AudioBookSelectedTextBookMenuViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_book_selected_text_book_menu)
        HeaderManager(findViewById(R.id.header)).setup(getString(R.string.details))
        val coverPath: String? = intent.getStringExtra("coverPath")
        val title: String = intent.getStringExtra("title").toString()
        val author: String = intent.getStringExtra("author").toString()
        val description: String = intent.getStringExtra("description").toString()
        println(coverPath != null)
        if (coverPath != null) {
            Picasso.get().load(coverPath).into(findViewById<ImageView>(R.id.cover))
        } else {
            findViewById<ImageView>(R.id.cover).setImageResource(R.drawable.generic_cover)
        }
        findViewById<TextView>(R.id.title).text = title
        findViewById<TextView>(R.id.author).text = author
        findViewById<TextView>(R.id.description).text = description

        val itemId = intent.getStringExtra("itemId")

        deleteButton = findViewById(R.id.delete)
        deleteButton.setOnClickListener {
            viewModel.deleteBook(this, itemId!!)
            finish()
        }

        readNowButton = findViewById(R.id.readNow)
        readNowButton.setOnClickListener {
//            val intent = Intent(this, AudioBookPlayerActivity::class.java)
//            intent.putExtra("bookName", title)
//            intent.putExtra("itemId", itemId)
//            startActivity(intent)
            val fileUrl =
                "/books/Elizabeth Gaskell/The life of Charlotter Bronte/elizabeth_gaskell-the_life_of_charlotte_bronte.epub"
            viewModel.downloadBook(this, fileUrl)
            val name = fileUrl.substring(fileUrl.lastIndexOf("/") + 1)
            val path = File(this.filesDir, name).absolutePath
            val reader: FolioReader = FolioReader.get()
            viewModel.book.observe(this) { book ->
                if (book) {
                    reader.openBook(path)
                }
            }
        }
    }
}