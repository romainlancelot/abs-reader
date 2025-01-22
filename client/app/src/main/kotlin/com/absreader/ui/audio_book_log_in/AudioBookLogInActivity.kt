package com.absreader.ui.audio_book_log_in

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.networks.models.LoginParameters
import com.absreader.repositories.LoginRepository
import com.absreader.utils.MaterialAlertDialog

class AudioBookLogInActivity : AppCompatActivity() {
    lateinit var username: String
    lateinit var password: String
    lateinit var login: Button
    lateinit var server: String
    private val repository: LoginRepository = LoginRepository()

    private fun gatherInputs() {
        username = findViewById<EditText>(R.id.username).text.toString()
        password = findViewById<EditText>(R.id.password).text.toString()
        server = findViewById<EditText>(R.id.server).text.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_book_log_in)
        login = findViewById<Button>(R.id.loginButton)
        login.setOnClickListener {
            gatherInputs()
            try {
                val loginParameters: LoginParameters = LoginParameters(username, password)
                repository.login(this@AudioBookLogInActivity, loginParameters, server)
            } catch (e: IllegalArgumentException) {
                MaterialAlertDialog.alert(
                    this@AudioBookLogInActivity, "Invalid server URL, please try again"
                )
                return@setOnClickListener
            }
        }
    }
}
