package com.church.ministry.ui.view.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.church.ministry.MainViewModel
import com.church.ministry.R
import com.church.ministry.data.model.Book
import com.church.ministry.ui.intent.MainIntent
import com.church.ministry.ui.view.PDFViewerActivity
import com.church.ministry.ui.view.fragment.home.BookClickListener
import com.church.ministry.ui.view.fragment.home.RowBookAdapter
import com.church.ministry.ui.viewState.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_serach.*
import kotlinx.android.synthetic.main.activity_serach.recyclerView
import kotlinx.android.synthetic.main.activity_serach.searchTxt
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), BookClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serach)
        searchTxt.requestFocus()


        recyclerView.adapter = rowAdapter

        observeViewModel()

        searchTxt.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty())
                lifecycleScope.launch {
                    mainViewModel.userIntent.send(MainIntent.GetSearchBooks("%"+text.toString()+"%"))
                }
        }

        icBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun observeViewModel() {

        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainViewState.Idle -> {
                    }
                    is MainViewState.Loading -> {
                    }
                    is MainViewState.SearchBooksList -> {
                        bookList = it.bookList
                        renderList(it.bookList)
                    }is MainViewState.GetBookContent -> {
                    rowAdapter.updateBookStatus(it.bookContent.bookId)
                    val intent = Intent(this@SearchActivity, PDFViewerActivity::class.java)
                    intent.putExtra("bookName", it.bookContent.bookTitleEn)
                    intent.putExtra("bookId", it.bookContent.bookId)
                    startActivity(intent)
                }
                    is MainViewState.Error -> {
                        Toast.makeText(this@SearchActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun renderList(bookList: List<Book>) {

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = rowAdapter
        recyclerView.visibility = View.VISIBLE
        bookList.let { rowAdapter.addData(false, it)}
        rowAdapter.notifyDataSetChanged()
    }

    //region variable
    private val mainViewModel: MainViewModel by viewModels()
    private var rowAdapter = RowBookAdapter(this, arrayListOf())
    private var bookList: List<Book> = arrayListOf()

    override fun bookClickListener(bookId: Int) {
        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.GetBookContent(bookId))
        }
    }
    // endregion
}