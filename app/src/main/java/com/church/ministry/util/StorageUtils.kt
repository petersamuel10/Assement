package com.church.ministry.util

import android.content.Context
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.nio.file.Files

object StorageUtils {


    fun getTextFromStorage(
        context: Context,
        fileName: String
    ): String {

        val file = createOrGetFile(context, fileName)
        return readFile(context, file)
    }


    fun setTextInStorage(
        context: Context,
        fileName: String,
        text: String
    ) {

        val file = createOrGetFile(context, fileName)
        writeFile(context, text, file)
    }


    fun createOrGetFile(
        context: Context,
        fileName: String
    ): File {

        val folder = File(context.filesDir, "books")
        return File(folder, fileName)
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

    private fun writeFile(context: Context, text: String, file: File) {
        try {
            file.parentFile.mkdir()
            file.bufferedWriter().use { out ->
                out.write(text)
            }

        } catch (e: IOException) {
            Toast.makeText(context, "Error write File", Toast.LENGTH_LONG).show()
        }

        Toast.makeText(context, "saved File", Toast.LENGTH_LONG).show()
    }

    fun deleteBookFiles(context: Context) {

        val folder = File(context.filesDir, "books")
        val files = folder.listFiles()
        if (files != null) for (file in files) {
            file.delete()
        }
    }

    fun createPDFFile(context: Context, decodedString: ByteArray, fileNameDb: String) {

        val file = createOrGetFile(context, "$fileNameDb.pdf")

        try {
            //val encoded = Files.readAllBytes(Paths.get(path))
            Files.write(file.toPath(), decodedString)
        } catch (e: IOException) {

        }
    }


}
