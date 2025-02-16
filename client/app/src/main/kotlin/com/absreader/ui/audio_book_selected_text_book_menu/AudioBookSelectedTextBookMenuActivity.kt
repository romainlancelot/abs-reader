package com.absreader.ui.audio_book_selected_text_book_menu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.ui.audio_book_player.AudioBookPlayerActivity
import com.absreader.utils.HeaderManager
import com.squareup.picasso.Picasso

class AudioBookSelectedTextBookMenuActivity : AppCompatActivity() {

    private lateinit var readNowButton: Button

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

        readNowButton = findViewById(R.id.readNow)
        readNowButton.setOnClickListener {
            val itemId = intent.getStringExtra("itemId")
            val intent = Intent(this, AudioBookPlayerActivity::class.java)
            intent.putExtra("bookName", title)
            intent.putExtra("itemId", itemId)
            startActivity(intent)
        }
    }
}