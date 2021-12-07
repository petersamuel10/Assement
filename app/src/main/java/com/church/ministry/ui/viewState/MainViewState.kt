package com.church.ministry.ui.viewState

import com.church.ministry.data.model.Book
import com.church.ministry.data.model.BookContent
import com.church.ministry.data.model.CheckNewBookResponse
import com.church.ministry.data.model.User

sealed class MainViewState {

    object Idle : MainViewState()
    object Loading : MainViewState()
    data class Login(val user: User) : MainViewState()
    class Logout(logout: Unit) : MainViewState()
    data class GetUserInfo(val user: User) : MainViewState()
    data class CheckNewBooks(val newBooksStatus: CheckNewBookResponse) : MainViewState()
    data class LocalBooksList(val bookList: List<Book>) : MainViewState()
    data class BooksList(val bookList: List<Book>) : MainViewState()
    data class SearchBooksList(val bookList: List<Book>) : MainViewState()
    data class GetBookContent(val bookContent: BookContent) : MainViewState()
    data class Error(val error: String?) : MainViewState()

}