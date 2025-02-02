package com.absreader.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.absreader.ui.audio_book_home.AudioBookHomeActivity
import com.absreader.ui.audio_book_log_in.AudioBookLogInActivity
import com.absreader.ui.text_book_home.TextBookHomeActivity
import com.absreader.ui.text_book_log_in.TextBookLogInActivity

class NavigationUtils {

    companion object {

        const val AUTH_SHARED_PREFERENCES_KEY_FILE: String = "auth"

        /*------------------------*\
        | TEXT BOOK API NAVIGATION |
        \*------------------------*/

        const val TEXT_BOOK_API_JWT_SHARED_PREFERENCES_KEY: String = "text_book_api_jwt"

        fun hasTextBookApiJwt(context: Context): Boolean {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(
                    AUTH_SHARED_PREFERENCES_KEY_FILE,
                    Context.MODE_PRIVATE
                )

            val jwt: String? = sharedPreferences.getString(
                TEXT_BOOK_API_JWT_SHARED_PREFERENCES_KEY,
                null
            )

            return jwt != null
        }

        fun redirectToTextBookLogInView(context: Context) {
            val intent = Intent(context, TextBookLogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun redirectToTextBookHomeView(context: Context) {
            val intent = Intent(context, TextBookHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        private fun deleteTextBookApiJwt(context: Context) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(
                    AUTH_SHARED_PREFERENCES_KEY_FILE,
                    Context.MODE_PRIVATE
                )

            sharedPreferences.edit().remove(TEXT_BOOK_API_JWT_SHARED_PREFERENCES_KEY).apply()
        }

        fun logOutFromTextBook(context: Context) {
            deleteTextBookApiJwt(context)
            redirectToTextBookLogInView(context)
        }

        /*-------------------------*\
        | AUDIO BOOK API NAVIGATION |
        \*-------------------------*/

        const val AUDIO_BOOK_API_JWT_SHARED_PREFERENCES_KEY: String = "audio_book_api_jwt"

        private fun hasAudioBookApiJwt(context: Context): Boolean {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(
                    AUTH_SHARED_PREFERENCES_KEY_FILE,
                    Context.MODE_PRIVATE
                )

            val jwt: String? = sharedPreferences.getString(
                AUDIO_BOOK_API_JWT_SHARED_PREFERENCES_KEY,
                null
            )

            return jwt != null
        }

        fun redirectToAudioBookLogInView(context: Context) {
            val intent = Intent(context, AudioBookLogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun redirectToAudioBookHomeView(context: Context) {
            val intent = Intent(context, AudioBookHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        private fun deleteAudioBookApiJwt(context: Context) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(
                    AUTH_SHARED_PREFERENCES_KEY_FILE,
                    Context.MODE_PRIVATE
                )

            sharedPreferences.edit().remove(AUDIO_BOOK_API_JWT_SHARED_PREFERENCES_KEY).apply()
        }

        fun logOutFromAudioBook(context: Context) {
            deleteAudioBookApiJwt(context)
            redirectToAudioBookLogInView(context)
        }
    }
}
