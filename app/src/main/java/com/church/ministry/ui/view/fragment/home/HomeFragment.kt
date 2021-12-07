package com.church.ministry.ui.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.church.ministry.MainViewModel
import com.church.ministry.R
import com.church.ministry.base.BaseFragment
import com.church.ministry.data.model.Book
import com.church.ministry.ui.intent.MainIntent
import com.church.ministry.ui.view.PDFViewerActivity
import com.church.ministry.ui.view.search.SearchActivity
import com.church.ministry.ui.viewState.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment(), BookClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.GetLocalBooksList)
        }

        recyclerView.adapter = gridAdapter

        observeViewModel()

        icGrid.setOnClickListener {
            icGrid.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple))
            icRow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
            renderListGrid(bookList)
        }
        icRow.setOnClickListener {
            icRow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple));
            icGrid.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey));
            renderList(bookList)
        }
        syncLy.setOnClickListener {
            getBookList(if (bookList.isNotEmpty()) bookList.last().bookId else 0)
        }

        searchTxt.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    SearchActivity::class.java
                )
            )
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainViewState.Idle -> {
                    }
                    is MainViewState.Loading -> {
                        if (newBooks)
                            progressDialog.show()
                    }
                    is MainViewState.LocalBooksList -> {
                        progressDialog.dismiss()
                        bookList.addAll(it.bookList)
                        renderListGrid(it.bookList)
                        checkNewBooks(if (it.bookList.isNotEmpty()) it.bookList.last().bookId else 0)
                    }
                    is MainViewState.CheckNewBooks -> {
                        if (it.newBooksStatus.success == "true" && it.newBooksStatus.data > 0)
                            syncLy.visibility = View.VISIBLE
                        else
                            syncLy.visibility = View.GONE
                    }
                    is MainViewState.BooksList -> {
                        progressDialog.dismiss()
                        newBooks = false
                        syncLy.visibility = View.GONE
                        bookList.addAll(it.bookList)
                        gridAdapter.addData(true, it.bookList)
                        rowAdapter.addData(true, it.bookList)
                    }
                    is MainViewState.GetBookContent -> {
                        rowAdapter.updateBookStatus(it.bookContent.bookId)
                        gridAdapter.updateBookStatus(it.bookContent.bookId)
                        val intent = Intent(requireContext(), PDFViewerActivity::class.java)
                        intent.putExtra("bookName", it.bookContent.bookTitleEn)
                        intent.putExtra("bookId", it.bookContent.bookId)
                        startActivity(intent)
                    }
                    is MainViewState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun checkNewBooks(lastId: Int) {
        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.CheckNewBooks(lastId))
        }
    }

    private fun getBookList(lastId: Int) {
        newBooks = true
        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.GetBooksList(lastId))
        }
    }

    private fun renderListGrid(bookList: List<Book>) {

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = gridAdapter
        recyclerView.visibility = View.VISIBLE
        bookList.let { gridAdapter.addData(false, it) }
    }

    private fun renderList(bookList: List<Book>) {

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = rowAdapter
        recyclerView.visibility = View.VISIBLE
        bookList.let { rowAdapter.addData(false, it) }
    }

    override fun bookClickListener(bookId: Int) {
        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.GetBookContent(bookId))
        }
    }

    //region variable
    private val mainViewModel: MainViewModel by viewModels()
    private var gridAdapter = MainAdapter(this, arrayListOf())
    private var rowAdapter = RowBookAdapter(this, arrayListOf())
    private var newBooks = false
    private var bookList: ArrayList<Book> = arrayListOf()
    // endregion
}