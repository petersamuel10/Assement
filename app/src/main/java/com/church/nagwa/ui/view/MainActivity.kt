package com.church.ministry.ui.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.church.ministry.MainViewModel
import com.church.ministry.R
import com.church.ministry.base.BaseActivity
import com.church.ministry.data.model.ItemInfo
import com.church.ministry.ui.intent.MainIntent
import com.church.ministry.ui.viewState.MainViewState
import com.downloader.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.io.File

@InternalCoroutinesApi
@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainActivity : BaseActivity(), BookClickListener {

    private var gridAdapter = MainAdapter(this, arrayListOf())
    private var itemList: ArrayList<ItemInfo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PRDownloader.initialize(applicationContext)

        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.GetUserInfo)
        }

        observeViewModel()
    }

    private fun observeViewModel() {

        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainViewState.Idle -> {
                    }
                    is MainViewState.Loading -> {
                    }
                    is MainViewState.GetUserInfo -> {
                        recyclerView.adapter = gridAdapter
                        gridAdapter.addData(true, it.user)
                        Log.d("result2", it.user.toString())
                    }
                    is MainViewState.Error -> {
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun bookClickListener(item: ItemInfo) {

        if (item.type == "VIDEO")
            downfile(item.url, "/" + item.name + ".mp4")
        else
            downfile(item.url, "/" + item.name + ".pdf")
    }

    fun downfile(url: String, fileName: String) {

        val folder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

        PRDownloader.download(url, folder?.absolutePath, fileName)
            .build()
            .setOnStartOrResumeListener {
                OnStartOrResumeListener { Log.d("down22", "start") }
            }
            .setOnProgressListener {
                OnProgressListener { progress ->
                    var per =
                        (progress.currentBytes.toFloat() / progress.totalBytes.toFloat()) * 100.00
                    Log.d("down22", per.toString())
                    progressBar.progress = per.toInt()
                }
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    viewFile(folder?.absolutePath + fileName)

                    Log.d("down22", "complete")
                }

                override fun onError(error: Error?) {
                    Log.d("down22", error.toString())
                }
            })
    }

    private fun viewFile(uri: String) {

        // Intent(Intent.ACTION_VIEW, Uri.parse(pdf_url));
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(File(uri).toString()))
        Uri.parse(File(uri).toString())
        //  browserIntent.setDataAndType(Uri.parse(msg.getData()), Constants.MIME_PDF)

        val chooser = Intent.createChooser(browserIntent, "choose")
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK // optional


        startActivity(chooser)

    }

    // region
    private val mainViewModel: MainViewModel by viewModels()
    private val PERMISSIONS = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val PERMISSION_REQUEST_CODE = 1
    private val DOWNLOAD_FILE_CODE = 2
    //endregion
}