package com.absreader.ui.hub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.ui.audio_book_log_in.AudioBookLogInActivity
import com.absreader.ui.text_book_log_in.TextBookLogInActivity

class HubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub)
        val textBookLoginButton: Button = findViewById(R.id.textBookLoginButton)
        val audioBookLoginButton: Button = findViewById(R.id.audioBookLoginButton)
        textBookLoginButton.setOnClickListener {
            val intent = Intent(this, TextBookLogInActivity::class.java)
            startActivity(intent)
            finish()
        }
        audioBookLoginButton.setOnClickListener {
            val intent = Intent(this, AudioBookLogInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
