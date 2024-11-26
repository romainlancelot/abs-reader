package com.absreader.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.login.models.LoginParameters
import com.absreader.networks.RetrofitFactory
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val server: String = findViewById<EditText>(R.id.server).toString()
        val username: String = findViewById<EditText>(R.id.username).toString()
        val password: String = findViewById<EditText>(R.id.password).toString()
        val login: Button = findViewById<Button>(R.id.loginButton)

        login.setOnClickListener {
            val client: Retrofit = RetrofitFactory.get_instance(server)
            val loginParameters: LoginParameters = LoginParameters(username, password)
        }
    }
}