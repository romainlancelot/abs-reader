package com.absreader.utils

import android.content.Intent
import android.view.View
import android.widget.TextView
import com.absreader.R

class HeaderManager(private val view: View, private val isReturnVisible: Boolean = true) {

    private val title: TextView = view.findViewById(R.id.headerTitle)
    private val returnButton: View = view.findViewById(R.id.returnButton)
    private val logoutButton: View = view.findViewById(R.id.logoutButton)

    fun setup(value: String): Unit {
        title.text = value
        returnButton.visibility = if (isReturnVisible) View.VISIBLE else View.INVISIBLE
        returnButton.setOnClickListener {
            (view.context as android.app.Activity).finish()
        }
        logoutButton.setOnClickListener {
            view.context
                .getSharedPreferences("absreader", android.content.Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
            val intent: Intent = Intent(view.context, com.absreader.LoginActivity::class.java)
            view.context.startActivity(intent)
        }
    }

}
