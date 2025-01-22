package com.absreader

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.absreader.ui.audio_book_home.AudioBookHomeActivity
import com.absreader.ui.audio_book_log_in.AudioBookLogInActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPreferences: SharedPreferences = getSharedPreferences("absreader", MODE_PRIVATE)
        val bearer: String = sharedPreferences.getString("bearer", "").toString()
        val server: String = sharedPreferences.getString("server", "").toString()
        if (bearer.isEmpty() || server.isEmpty()) {
            val intent: Intent = Intent(this, AudioBookLogInActivity::class.java)
            startActivity(intent)
        } else {
            val intent: Intent = Intent(this, AudioBookHomeActivity::class.java)
            startActivity(intent)
        }
    }
}
