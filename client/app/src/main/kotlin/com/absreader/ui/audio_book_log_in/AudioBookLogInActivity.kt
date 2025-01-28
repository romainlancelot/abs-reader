package com.absreader.ui.audio_book_log_in

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.data.model.auth.AudioBookAuthLogin
import com.absreader.data.repository.AudioBookAuthRepository
import com.absreader.ui.audio_book_home.AudioBookHomeActivity
import com.absreader.ui.hub.HubActivity
import com.absreader.utils.MaterialAlertDialog

class AudioBookLogInActivity : AppCompatActivity() {
    lateinit var username: String
    lateinit var password: String
    lateinit var login: Button
    lateinit var server: String
    private val repository: AudioBookAuthRepository = AudioBookAuthRepository()

    private fun gatherInputs() {
        username = findViewById<EditText>(R.id.username).text.toString()
        password = findViewById<EditText>(R.id.password).text.toString()
        server = findViewById<EditText>(R.id.server).text.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (isLoggedIn()) {
            goToAudioBookHomeActivity()
            return
        }
        setContentView(R.layout.activity_audio_book_log_in)
        login = findViewById<Button>(R.id.loginButton)
        login.setOnClickListener {
            gatherInputs()
            try {
                val audioBookAuthLogin: AudioBookAuthLogin = AudioBookAuthLogin(username, password)
                repository.login(this@AudioBookLogInActivity, audioBookAuthLogin, server)
            } catch (e: IllegalArgumentException) {
                MaterialAlertDialog.alert(
                    this@AudioBookLogInActivity, "Invalid server URL, please try again"
                )
                return@setOnClickListener
            }
        }

        val backToHubButton: Button = findViewById(R.id.backToHubButton)
        backToHubButton.setOnClickListener {
            val intent = Intent(this, HubActivity::class.java)
            startActivity(intent)
        }
    }

    fun isLoggedIn(): Boolean {
        val sharedPreferences: SharedPreferences = getSharedPreferences("absreader", MODE_PRIVATE)
        val bearer: String = sharedPreferences.getString("bearer", "").toString()
        return bearer.isNotEmpty()
    }

    fun goToAudioBookHomeActivity() {
        val intent: Intent = Intent(this, AudioBookHomeActivity::class.java)
        startActivity(intent)
    }
}
