package com.absreader.ui.hub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.absreader.R
import com.absreader.ui.text_book_log_in.TextBookLogInFragment

class HubFragment : Fragment(R.layout.fragment_hub) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val otherButton = view.findViewById<Button>(R.id.otherButton)

        loginButton.setOnClickListener {
            val intent = Intent(
                requireContext(),
                TextBookLogInFragment::class.java
            )
            startActivity(intent)
        }

        otherButton.setOnClickListener { }
    }
}
