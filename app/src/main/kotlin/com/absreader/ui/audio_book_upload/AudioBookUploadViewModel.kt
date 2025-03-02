package com.absreader.ui.audio_book_upload

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.absreader.data.network.AudioBookRetrofitClient
import com.absreader.data.network.service.AudioBookUploadService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AudioBookUploadViewModel : ViewModel() {
    private val _uploadStatus = MutableLiveData<UploadStatus>()
    val uploadStatus: LiveData<UploadStatus> = _uploadStatus

    private val _uploadProgress = MutableLiveData<Int>()
    val uploadProgress: LiveData<Int> = _uploadProgress

    fun uploadBook(
        context: Context,
        fileUri: Uri,
        title: String,
        author: String,
        series: String,
        libraryId: String,
        folderId: String
    ) {
        _uploadStatus.value = UploadStatus.UPLOADING

        try {
            val inputStream = context.contentResolver.openInputStream(fileUri)
            val file = createTempFileFromUri(context, inputStream, fileUri)

            val filePart = prepareFilePart(file)
            val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val authorPart = author.toRequestBody("text/plain".toMediaTypeOrNull())
            val seriesPart = series.toRequestBody("text/plain".toMediaTypeOrNull())
            val libraryPart = libraryId.toRequestBody("text/plain".toMediaTypeOrNull())
            val folderPart = folderId.toRequestBody("text/plain".toMediaTypeOrNull())

            val client = AudioBookRetrofitClient.getInstance(context)
            val uploadService = client.create(AudioBookUploadService::class.java)

            uploadService.uploadBook(filePart, titlePart, authorPart, seriesPart, libraryPart, folderPart)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            _uploadStatus.postValue(UploadStatus.SUCCESS)
                        } else {
                            _uploadStatus.postValue(UploadStatus.ERROR)
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        _uploadStatus.postValue(UploadStatus.ERROR)
                    }
                })
        } catch (e: Exception) {
            _uploadStatus.value = UploadStatus.ERROR
        }
    }

    private fun createTempFileFromUri(context: Context, inputStream: InputStream?, fileUri: Uri): File {
        val fileName = getFileName(context, fileUri) ?: "temp_file"
        val file = File(context.cacheDir, fileName)

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }

        return file
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex("_display_name")
                    if (columnIndex != -1) {
                        result = it.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }

    private fun prepareFilePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("0", file.name, requestFile)
    }

    enum class UploadStatus {
        UPLOADING,
        SUCCESS,
        ERROR
    }
}