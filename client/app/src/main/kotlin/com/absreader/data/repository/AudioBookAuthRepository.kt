package com.absreader.data.repository

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.absreader.ui.audio_book_home.AudioBookHomeActivity
import com.absreader.data.network.AudioBookRetrofitClient
import com.absreader.data.network.dto.audio_book_auth.LoginDTO
import com.absreader.data.model.auth.AudioBookAuthLogin
import com.absreader.data.network.service.AudioBookAuthService
import com.absreader.utils.MaterialAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AudioBookAuthRepository {
    fun login(
        context: Context,
        audioBookAuthLogin: AudioBookAuthLogin,
        server: String
    ) {
        val client: Retrofit = AudioBookRetrofitClient.getInstance(context, server)
        val call: Call<LoginDTO> =
            client
                .create(AudioBookAuthService::class.java)
                .login(audioBookAuthLogin)
        call.enqueue(object : Callback<LoginDTO> {
            override fun onResponse(call: Call<LoginDTO>, response: Response<LoginDTO>) {
                if (response.isSuccessful) {
                    val loginDTO: LoginDTO? = response.body()
                    with(context.getSharedPreferences("absreader", MODE_PRIVATE).edit()) {
                        putString("bearer", "Bearer ${loginDTO?.user?.token}");
                        putString("server", server);
                        apply();
                    }
                    val intent: Intent = Intent(context, AudioBookHomeActivity::class.java);
                    context.startActivity(intent);
                    return;
                }
                MaterialAlertDialog.alert(context, "Invalid credentials, please try again");
            }

            override fun onFailure(call: Call<LoginDTO>, t: Throwable) {
                MaterialAlertDialog.alert(context, "Error with the server, try again");
            }
        })
    }
}
