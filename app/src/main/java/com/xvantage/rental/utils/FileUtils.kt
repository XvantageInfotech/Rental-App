package com.xvantage.rental.utils

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileUtils private constructor() : Parcelable {

    private var progress: Int = 0
    private var currentFileSize: Int = 0
    private var totalFileSize: Int = 0

    companion object {
        private const val TAG = "FileUtils"
        private var instance: FileUtils? = null

        @JvmField
        val CREATOR: Parcelable.Creator<FileUtils> = object : Parcelable.Creator<FileUtils> {
            override fun createFromParcel(parcel: Parcel): FileUtils {
                return FileUtils(parcel)
            }

            override fun newArray(size: Int): Array<FileUtils?> {
                return arrayOfNulls(size)
            }
        }

        fun getInstance(): FileUtils {
            if (instance == null) {
                instance = FileUtils()
            }
            return instance!!
        }
    }

    private constructor(parcel: Parcel) : this() {
        progress = parcel.readInt()
        currentFileSize = parcel.readInt()
        totalFileSize = parcel.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(progress)
        dest.writeInt(currentFileSize)
        dest.writeInt(totalFileSize)
    }

    fun createTempImageFile(context: Context): File? {
        val outputDir = context.cacheDir
        return try {
            val timeStamp = System.currentTimeMillis()
            File.createTempFile("tempImage_$timeStamp", ".png", outputDir)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun saveBitmapToFile(bitmap: Bitmap, filePath: String): String {
        val file = File(filePath)
        if (file.exists()) file.delete()
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return uri.path
    }

    fun getPath(context: Context, uri: Uri): String {
        return getLocalPath(context, uri) ?: uri.toString()
    }

    private fun getLocalPath(context: Context, uri: Uri): String? {
        Log.d(
            TAG,
            "File - Authority: ${uri.authority}, Fragment: ${uri.fragment}, Port: ${uri.port}, Query: ${uri.query}, Scheme: ${uri.scheme}, Host: ${uri.host}, Segments: ${uri.pathSegments}"
        )

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isLocalStorageDocument(uri)) {
                return DocumentsContract.getDocumentId(uri)
            } else if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]

                return if ("primary" == type) {
                    Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                } else {
                    "${Environment.getExternalStorageDirectory()}/documents/${split[1]}"
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4)
                }

                val contentUriPrefixesToTry = arrayOf(
                    "content://downloads/public_downloads",
                    "content://downloads/my_downloads"
                )

                for (contentUriPrefix in contentUriPrefixesToTry) {
                    val contentUri =
                        ContentUris.withAppendedId(Uri.parse(contentUriPrefix), id.toLong())
                    try {
                        val path = getDataColumn(context, contentUri, null, null)
                        if (path != null) return path
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val fileName = getFileName(context, uri)
                val cacheDir = getDocumentCacheDir(context)
                val file = generateFileName(fileName, cacheDir)
                val destinationPath = file?.absolutePath
                destinationPath?.let { saveFileFromUri(context, uri, it) }
                return destinationPath
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val type = split[0]
                val contentUri: Uri? = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> null
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return contentUri?.let { getDataColumn(context, it, selection, selectionArgs) }
            }
        }

        return if ("content" == uri.scheme) {
            if (isGooglePhotosUri(uri)) {
                uri.lastPathSegment
            } else {
                getDataColumn(context, uri, null, null)
            }
        } else if ("file" == uri.scheme) {
            uri.path
        } else {
            null
        }
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        context.contentResolver.query(uri, arrayOf("_data"), selection, selectionArgs, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow("_data")
                    return cursor.getString(columnIndex)
                }
            }
        return null
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null

        if ("content" == uri.scheme) {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex >= 0) {
                        result = cursor.getString(columnIndex)
                    }
                }
            }
        }
        return result ?: uri.path?.substringAfterLast(File.separator)
    }


    private fun getDocumentCacheDir(context: Context): File {
        val dir = context.cacheDir
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun generateFileName(name: String?, directory: File): File? {
        if (name == null) return null

        var file = File(directory, name)
        if (file.exists()) {
            var fileName = name
            val extension = name.substringAfterLast(".", "")
            fileName = name.substringBeforeLast(".")
            var index = 0

            while (file.exists()) {
                index++
                file = File(directory, "$fileName($index).$extension")
            }
        }

        return if (file.createNewFile()) file else null
    }

    private fun saveFileFromUri(context: Context, uri: Uri, destinationPath: String) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedOutputStream(FileOutputStream(destinationPath, false)).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isLocalStorageDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}
