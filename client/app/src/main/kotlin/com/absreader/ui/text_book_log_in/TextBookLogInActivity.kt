package com.absreader.ui.text_book_log_in

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.absreader.R
import com.absreader.data.repository.TextBookAuthRepository
import com.absreader.ui.hub.HubActivity
import com.absreader.ui.text_book_home.TextBookHomeActivity
import com.absreader.utils.NavigationUtils

class TextBookLogInActivity : AppCompatActivity() {

    private lateinit var viewModel: TextBookLogInViewModel

    override fun onCreate(savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_book_log_in)

        if (NavigationUtils.hasTextBookApiJwt(this)) {
            NavigationUtils.redirectToTextBookHomeView(this)
            return;
        }

        val repository = TextBookAuthRepository(application)
        val factory = TextBookLogInViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, factory)
                .get(TextBookLogInViewModel::class.java)

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val returnButton: Button = findViewById(R.id.returnButton)
        val loginButton: Button = findViewById(R.id.loginButton)

        returnButton.setOnClickListener {
            val intent = Intent(this, HubActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.login(email, password)
        }

        viewModel.hasLogInSucceeded.observe(this, Observer { result ->
            if (result) {
                val intent: Intent = Intent(this, TextBookHomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        viewModel.logInMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }
}
