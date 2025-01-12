package com.absreader

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkAndRequestPermissions()
    }

    // Check if I have permissions
    private fun checkAndRequestPermissions() {
        // The list of permissions I need. Here's the permission to read files on my device.
        // The permission MANAGE_EXTERNAL_STORAGE is available only on Android 30+ then I've changed the minSdk to 30
        val permissions = mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )

        // The list of permissions not yet granted.
        val permissionsToRequest = mutableListOf<String>()

        // I check if each permission is granted. Otherwise I add it in the list.
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }

        // I ask for each non granted permission remaining. Otherwise I display the menu.
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                0
            )
        } else {
            displayHomeMenu()
        }
    }

    private fun displayHomeMenu() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("absreader", MODE_PRIVATE)
        val bearer: String = sharedPreferences.getString("bearer", "").toString()
        val server: String = sharedPreferences.getString("server", "").toString()
        if (bearer.isEmpty() || server.isEmpty()) {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            val intent: Intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

}
