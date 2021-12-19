package com.church.nagwa.ui.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.church.nagwa.MainViewModel
import com.church.nagwa.R
import com.church.nagwa.base.BaseActivity
import com.church.nagwa.data.model.ItemInfo
import com.church.nagwa.ui.intent.MainIntent
import com.church.nagwa.ui.viewState.MainViewState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PRDownloader.initialize(applicationContext)
        folder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
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
                        itemList.addAll(it.itemList)
                        gridAdapter.addData(true, it.itemList)
                        Log.d("result2", it.itemList.toString())
                    }
                    is MainViewState.Error -> {
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun bookClickListener(item: ItemInfo, position: Int) {

        val fileName =
            if (item.type == "VIDEO") "/" + item.name + ".mp4" else "/" + item.name + ".pdf"

        if (itemList[position].isDown)
            viewFile(folder?.absolutePath + fileName)
        else
            downfile(item.url, fileName, item.id)

    }

    fun downfile(url: String, fileName: String, itemId: Int) {

        val folder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        PRDownloader.download(url, folder?.absolutePath, fileName)
            .build()
            .setOnProgressListener {
                OnProgressListener { progress ->
                    var per =
                        (progress.currentBytes.toFloat() / progress.totalBytes.toFloat()) * 100.00
                }
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {

                    gridAdapter.updateItemStatus(itemId)
                    viewFile(folder?.absolutePath + fileName)
                }

                override fun onError(error: Error?) {
                }
            })
    }

    private fun viewFile(uri: String) {

        val photoURI = FileProvider.getUriForFile(
            this,
            getApplicationContext().getPackageName().toString() + ".provider",
            File(uri)
        )
        val target = Intent(Intent.ACTION_VIEW)
        target.setDataAndType(photoURI, "application/*")
        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        val intent = Intent.createChooser(target, "Open File")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
        }

    }

    // region
    private val mainViewModel: MainViewModel by viewModels()
    private var gridAdapter = MainAdapter(this, arrayListOf())
    private var itemList: ArrayList<ItemInfo> = arrayListOf()
    var folder :File? = null
    //endregion
}