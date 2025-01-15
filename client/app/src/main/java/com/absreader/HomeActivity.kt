package com.absreader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.absreader.adapters.LibraryAdapter
import com.absreader.adapters.ProgressAdapter
import com.absreader.networks.dto.libraries.Library
import com.absreader.networks.dto.progress.LibraryItem
import com.absreader.utils.HeaderManager
import com.absreader.view_models.LibraryViewModel
import com.absreader.view_models.ProgressViewModel

class HomeActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100
    private val libraryViewModel: LibraryViewModel = LibraryViewModel()
    private val progressViewModel: ProgressViewModel = ProgressViewModel()

    // Check if I have permissions.
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
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    // This function is automatically called after the permissions request by Android.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            val deniedPermissions = permissions.zip(grantResults.toList()).filter {
                it.second != PackageManager.PERMISSION_GRANTED
            }.map { it.first }

            if (deniedPermissions.isEmpty()) {
//                displayHomeMenu()
            } else {
                Toast.makeText(
                    this,
                    "Permissions refusées : $deniedPermissions. L'application ne pourra pas fonctionner correctement.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        checkAndRequestPermissions()
        HeaderManager(findViewById(R.id.header), false).setup(getString(R.string.home))
        val noLibraryText: TextView = findViewById(R.id.noLibraries)
        val noProgressText: TextView = findViewById(R.id.noProgress)
        val libraryRecyclerView: RecyclerView = findViewById(R.id.libraryRecyclerView)
        libraryRecyclerView.layoutManager = GridLayoutManager(this, 1)
        this.libraryViewModel.libraries.observe(this) { libraries: List<Library> ->
            libraryRecyclerView.adapter = LibraryAdapter(libraries)
            if (libraries.isEmpty()) {
                noLibraryText.visibility = TextView.VISIBLE
            }
        }
        this.libraryViewModel.getLibraries(this@HomeActivity)
        val progressRecyclerView: RecyclerView = findViewById(R.id.progressRecyclerView)
        progressRecyclerView.layoutManager = GridLayoutManager(this, 2)
        this.progressViewModel.progress.observe(this) { progress: List<LibraryItem> ->
            progressRecyclerView.adapter = ProgressAdapter(progress)
            if (progress.isEmpty()) {
                noProgressText.visibility = TextView.VISIBLE
            }
        }
        this.progressViewModel.getProgress(this@HomeActivity)
    }
}