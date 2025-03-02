package com.absreader.utils

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.absreader.R
import com.absreader.ui.audio_book_log_in.AudioBookLogInActivity
import com.absreader.ui.audio_book_player.AudioBookPlayerService

class HeaderManager(private val view: View, private val returnVisible: Boolean = true) {

    private val title: TextView = view.findViewById(R.id.headerTitle)
    private val returnButton: View = view.findViewById(R.id.returnButton)
    private val logoutButton: View = view.findViewById(R.id.logoutButton)

    fun setup(value: String): Unit {
        title.text = value
        returnButton.visibility = if (returnVisible) View.VISIBLE else View.INVISIBLE

        returnButton.setOnClickListener {
            stopAudioService()
            (view.context as android.app.Activity).finish()
        }
        logoutButton.setOnClickListener {
            view.context.getSharedPreferences("auth", android.content.Context.MODE_PRIVATE).edit().clear().apply()
            stopAudioService()
            (view.context as android.app.Activity).finish()
            val intent = Intent(view.context, AudioBookLogInActivity::class.java)
            view.context.startActivity(intent)
        }
    }

    private fun stopAudioService() {
        val stopIntent = Intent(view.context, AudioBookPlayerService::class.java)
        view.context.stopService(stopIntent)
    }

}
