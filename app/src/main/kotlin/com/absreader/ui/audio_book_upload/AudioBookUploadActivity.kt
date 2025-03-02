package com.absreader.ui.audio_book_upload

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.absreader.R
import com.absreader.utils.HeaderManager
import com.google.android.material.textfield.TextInputEditText
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.absreader.data.network.dto.audio_book_libraries.Folder
import com.google.android.material.textfield.TextInputLayout

class AudioBookUploadActivity : AppCompatActivity() {
    private val viewModel = AudioBookUploadViewModel()
    private var selectedFileUri: Uri? = null

    private lateinit var titleInput: TextInputEditText
    private lateinit var authorInput: TextInputEditText
    private lateinit var seriesInput: TextInputEditText
    private lateinit var selectFileButton: Button
    private lateinit var selectedFileText: TextView
    private lateinit var uploadButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var folderLayout: TextInputLayout
    private lateinit var folderDropdown: AutoCompleteTextView
    private val folderList = mutableListOf<Folder>()
    private var selectedFolderId: String = ""

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val mimeType = contentResolver.getType(it)
                if (mimeType?.contains("audio/mpeg") == true || mimeType?.contains("application/epub+zip") == true) {
                    selectedFileUri = it
                    val fileName = getFileName(it) ?: this.getString(R.string.selected_file)
                    selectedFileText.text = fileName
                    uploadButton.isEnabled = true
                } else {
                    Toast.makeText(this, this.getString(R.string.unsupported_file), Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_book_upload)
        HeaderManager(findViewById(R.id.header)).setup(this.getString(R.string.upload_book))

        titleInput = findViewById(R.id.titleInput)
        authorInput = findViewById(R.id.authorInput)
        seriesInput = findViewById(R.id.seriesInput)
        selectFileButton = findViewById(R.id.selectFileButton)
        selectedFileText = findViewById(R.id.selectedFileText)
        uploadButton = findViewById(R.id.uploadButton)
        progressBar = findViewById(R.id.progressBar)
        folderDropdown = findViewById(R.id.folderDropdown)
        folderLayout = findViewById(R.id.folderLayout)

        val libraryId = intent.getStringExtra("libraryId") ?: ""
        parseFolders(intent.getStringExtra("libraryFolders") ?: "")
        setupFolderDropdown()

        selectFileButton.setOnClickListener {
            getContent.launch("*/*")
        }

        uploadButton.setOnClickListener {
            if (validateInputs()) {
                uploadBook(libraryId, selectedFolderId)
            }
        }

        viewModel.uploadStatus.observe(this) { status ->
            when (status) {
                AudioBookUploadViewModel.UploadStatus.UPLOADING -> {
                    progressBar.visibility = View.VISIBLE
                    uploadButton.isEnabled = false
                    selectFileButton.isEnabled = false
                }

                AudioBookUploadViewModel.UploadStatus.SUCCESS -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, this.getString(R.string.upload_successfully), Toast.LENGTH_LONG).show()
                    finish()
                }

                AudioBookUploadViewModel.UploadStatus.ERROR -> {
                    progressBar.visibility = View.INVISIBLE
                    uploadButton.isEnabled = true
                    selectFileButton.isEnabled = true
                    Toast.makeText(this, this.getString(R.string.upload_failed), Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }

        viewModel.uploadProgress.observe(this) { progress ->
            progressBar.progress = progress
        }
    }

    private fun validateInputs(): Boolean {
        if (titleInput.text.isNullOrBlank()) {
            titleInput.error = this.getString(R.string.title_required)
            return false
        }

        if (selectedFileUri == null) {
            Toast.makeText(this, this.getString(R.string.selected_file), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun uploadBook(libraryId: String, folderId: String) {
        selectedFileUri?.let { fileUri ->
            val title = titleInput.text.toString()
            val author = authorInput.text.toString()
            val series = seriesInput.text.toString()

            viewModel.uploadBook(
                context = this,
                fileUri = fileUri,
                title = title,
                author = author,
                series = series,
                libraryId = libraryId,
                folderId = selectedFolderId
            )
        }
    }

    private fun getFileName(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex("_display_name")
                if (columnIndex != -1) {
                    return it.getString(columnIndex)
                }
            }
        }
        return null
    }

    private fun parseFolders(foldersString: String) {
        folderList.clear()

        // parsing of the folder string
        val regex = Regex("fullPath=([^,]+).*?id=([^,\\)]+)")
        val matches = regex.findAll(foldersString)

        for (match in matches) {
            val fullPath = match.groupValues[1]
            val id = match.groupValues[2]
            folderList.add(Folder(0, fullPath, id, ""))
        }

        if (folderList.isNotEmpty()) {
            selectedFolderId = folderList[0].id
        }
    }

    private fun setupFolderDropdown() {
        val folderNames = folderList.map { it.fullPath }.toTypedArray()

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, folderNames)
        folderDropdown.setAdapter(adapter)

        if (folderList.isNotEmpty()) {
            folderDropdown.setText(folderList[0].fullPath, false)
        }

        folderDropdown.setOnItemClickListener { _, _, position, _ ->
            selectedFolderId = folderList[position].id
        }
    }
}