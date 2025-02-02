package com.absreader.ui.hub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.ui.audio_book_log_in.AudioBookLogInActivity
import com.absreader.ui.text_book_log_in.TextBookLogInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_text_book -> {
                    val intent = Intent(this, TextBookLogInActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_audio_book -> {
                    val intent = Intent(this, AudioBookLogInActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

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
