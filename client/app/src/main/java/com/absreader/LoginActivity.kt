package com.absreader

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.absreader.networks.models.LoginParameters
import com.absreader.repositories.LoginRepository
import com.absreader.utils.MaterialAlertDialog

class LoginActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_login)
        login = findViewById<Button>(R.id.loginButton)
        login.setOnClickListener {
            gatherInputs()
            try {
                val loginParameters: LoginParameters = LoginParameters(username, password)
                repository.login(this@LoginActivity, loginParameters, server)
            } catch (e: IllegalArgumentException) {
                MaterialAlertDialog.alert(
                    this@LoginActivity, "Invalid server URL, please try again"
                )
                return@setOnClickListener
            }
        }
    }
}