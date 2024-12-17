package com.absreader.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.home.HomeActivity
import com.absreader.login.models.LoginParameters
import com.absreader.networks.RetrofitFactory
import com.absreader.networks.dto.login.LoginDTO
import com.absreader.networks.services.AuthenticationService
import com.absreader.utils.MaterialAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class LoginActivity : AppCompatActivity() {
    lateinit var username: String
    lateinit var password: String
    lateinit var login: Button
    lateinit var server: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        login = findViewById<Button>(R.id.loginButton)
        login.setOnClickListener {
            gatherInputs()
            try {
                val client: Retrofit = RetrofitFactory.getInstance(server)
                val loginParameters: LoginParameters = LoginParameters(username, password)
                val call: Call<LoginDTO> = client.create(AuthenticationService::class.java).login(
                    loginParameters
                )
                login(call)
            } catch (e: IllegalArgumentException) {
                MaterialAlertDialog.alert(
                    this@LoginActivity, "Invalid server URL, please try again"
                )
                return@setOnClickListener
            }

        }
    }

    private fun gatherInputs() {
        username = findViewById<EditText>(R.id.username).text.toString()
        password = findViewById<EditText>(R.id.password).text.toString()
        server = findViewById<EditText>(R.id.server).text.toString()
    }

    fun login(call: Call<LoginDTO>) {
        call.enqueue(object : Callback<LoginDTO> {
            override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                if (response.isSuccessful) {
                    val loginDTO: LoginDTO? = response.body()
                    with(getSharedPreferences("absreader", MODE_PRIVATE).edit()) {
                        putString("bearer", "Bearer ${loginDTO?.user?.token}")
                        putString("server", "$server/api/")
                        apply()
                    }
                    val intent: Intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    return
                }
                MaterialAlertDialog.alert(
                    this@LoginActivity, "Invalid credentials, please try again"
                )
            }

            override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                MaterialAlertDialog.alert(
                    this@LoginActivity, "Error with the server, try again"
                )
            }
        })
    }
}