package com.church.ministry.util

import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.IOException

object StorageUtils {

    fun createOrGetFile(
        context: Context,
        fileName: String
    ): File {

        val folder = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        return File(folder, fileName)
    }

    fun getTextFromStorage(
        context: Context,
        fileName: String
    ): String {

        val file = createOrGetFile(context, fileName)
        return readFile(context, file)
    }


    private fun readFile(context: Context, file: File): String {
        val sb = StringBuilder()
        if (file.exists()) {
            try {
                val bufferedReader = file.bufferedReader()
                bufferedReader.useLines { lines ->
                    lines.forEach {
                        sb.append(it)
                        sb.append("\n")
                    }
                }

            } catch (e: IOException) {
                Toast.makeText(context, "Error read File", Toast.LENGTH_LONG).show()
            }
        }
        return sb.toString()
    }


}
