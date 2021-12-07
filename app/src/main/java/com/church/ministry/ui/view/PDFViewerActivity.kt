package com.church.ministry.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.artifex.mupdf.viewer.DocumentActivity
import com.church.ministry.MainViewModel
import com.church.ministry.R
import com.church.ministry.base.BaseActivity
import com.church.ministry.ui.intent.MainIntent
import com.church.ministry.ui.viewState.MainViewState
import com.church.ministry.util.StorageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_p_d_f_viewer.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class PDFViewerActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_d_f_viewer)

        val bookId = intent.getIntExtra("bookId", 0)
        bookTitle.text = intent.getStringExtra("bookName")

       // lifecycleScope.launch { mainViewModel.userIntent.send(MainIntent.GetBookContent(bookId)) }

        startMuPDFActivityWithExampleFile()

//        val renderer = PdfRenderer("")
//
//        // let us just render all pages
//
//        // let us just render all pages
//        val pageCount = renderer.pageCount
//        for (i in 0 until pageCount) {
//            val page: PdfRenderer.Page = renderer.openPage(i)
//
//            // say we render for showing on the screen
//            page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//
//            // do stuff with the bitmap
//
//            // close the page
//            page.close()
//        }
//
//        // close the renderer
//
//        // close the renderer
//        renderer.close()

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainViewState.Idle -> {

                    }
                    is MainViewState.Loading -> {
                        progressDialog.show()
                    }

                    is MainViewState.GetBookContent -> {
                        progressDialog.dismiss()
                        renderPdf(
                            readFromStorage(it.bookContent.fileNameDb),
                            it.bookContent.fileNameDb
                        )
                    }
                    is MainViewState.Error -> {
                        progressDialog.dismiss()
                        Log.d("ex111", it.error.toString())
                        Toast.makeText(this@PDFViewerActivity, it.error, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    private fun readFromStorage(fileName: String): String {
        return StorageUtils.getTextFromStorage(this, fileName)
    }

    private fun renderPdf(bookContent: String, fileNameDb: String) {

        try {
            val decodedString = Base64.decode(bookContent, Base64.DEFAULT)
            StorageUtils.createPDFFile(this, decodedString, fileNameDb)
            pdfView.fromFile(StorageUtils.createOrGetFile(this, fileNameDb))
                .enableSwipe(true) // allows to block changing pages using swipe
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .autoSpacing(true)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(40)
                .fitEachPage(true)
                .load()


         //   intent.setData(documentUri);


        } catch (e: Exception) {
            Log.d("exx12345", e.message.toString())
        }





//        PDFView.Configurator.onRender(object : OnRenderListener {
//            fun onInitiallyRendered(pages: Int, pageWidth: Float, pageHeight: Float) {
//                pdfView.fitToWidth(pageIndex)
//            }
//        })
    }

    fun startMuPDFActivityWithExampleFile() {
        val dir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(dir, "test.pdf")
        val uri = Uri.fromFile(file)
        startMuPDFActivity(uri)
    }

    private fun startMuPDFActivity(uri: Uri) {

        val intent = Intent(this, DocumentActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        intent.data = uri
        startActivity(intent)

//        val intent = Intent(this, DocumentActivity::class.java)
//        intent.action = Intent.ACTION_VIEW
//        intent.data = Uri.fromFile(uri)
//        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        onBackPressed()
    }

}
//region
//                val encoded = Files.readAllBytes(bookContent.data.bookFileBase64.toByteArray())
//                Files.write(Paths.get(finalPath), encoded)

//            val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//            var file = File.createTempFile("my_file11",".pdf", path)
//            var os = FileOutputStream(file);
//            os.write(bookContent.data.bookFileBase64.toByteArray());
//            os.close();

//    pdfView.fromUri(Uri.fromFile(file)).load()

//        val bytcode: String = getString(R.string.fileContent)
//       // Base64.encodeToString("yourString".getBytes("UTF-8"), Base64.NO_WRAP)
//
//        val encodedString = Base64.getDecoder().decode(bytcode)

//  val decodedBytes = Base64.getDecoder().decode(getString(R.string.fileBase64))
//  val decodedString = String(decodedBytes)

//        val path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//        var file = File.createTempFile("my_file",".pdf", path)
//        var os = FileOutputStream(file);
//        os.write(decodedBytes);
//        os.close();

//endregion
