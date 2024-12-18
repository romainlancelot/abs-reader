package com.absreader.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
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

class LoginRepository {
    fun login(context: Context, loginParameters: LoginParameters, server: String) {
        val client: Retrofit = RetrofitFactory.getInstance(context, server)
        val call: Call<LoginDTO> = client.create(AuthenticationService::class.java)
            .login(loginParameters)
        call.enqueue(object : Callback<LoginDTO> {
            override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                if (response.isSuccessful) {
                    val loginDTO: LoginDTO? = response.body()
                    with(context.getSharedPreferences("absreader", MODE_PRIVATE).edit()) {
                        putString("bearer", "Bearer ${loginDTO?.user?.token}")
                        putString("server", server)
                        apply()
                    }
                    val intent: Intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent)
                    return
                }
                MaterialAlertDialog.alert(context, "Invalid credentials, please try again")
            }

            override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                MaterialAlertDialog.alert(context, "Error with the server, try again")
            }
        })
    }
}