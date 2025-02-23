package com.absreader.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.ui.audio_book_log_in.AudioBookLogInActivity
import com.absreader.ui.text_book_log_in.TextBookLogInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
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
    }
}