package com.church.ministry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.church.ministry.data.repository.MainRepository
import com.church.ministry.ui.intent.MainIntent
import com.church.ministry.ui.viewState.MainViewState
import com.church.ministry.util.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val repository: MainRepository,
) : ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainViewState>(MainViewState.Idle)
    val state: StateFlow<MainViewState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.GetUserInfo -> getUserInfo()
                    is MainIntent.Logout -> logout()
                    is MainIntent.CheckNewBooks -> checkNewBooks(it.lastId)
                    is MainIntent.GetLocalBooksList -> getBookList(false, 0)
                    is MainIntent.GetBooksList -> getBookList(true, it.lastId)
                    is MainIntent.GetBookContent -> getBookContent(it.bookId)
                    is MainIntent.GetSearchBooks -> getSearchBooks(it.searchTxt)
                }
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            _state.value = try {
                MainViewState.GetUserInfo(repository.getUserInfo())
            } catch (e: Exception) {
                MainViewState.Error(e.localizedMessage)
            }
        }
    }

    private fun checkNewBooks(lastId: Int) {
        if (networkHelper.isNetworkConnected())
            viewModelScope.launch {
                _state.value = MainViewState.Loading
                _state.value = try {
                    MainViewState.CheckNewBooks(repository.checkNewBooks(lastId))
                } catch (e: Exception) {
                    MainViewState.Error(e.localizedMessage)
                }
            }
    }

    private fun getBookList(online: Boolean, lastId: Int) {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            _state.value = try {
                if (online)
                    MainViewState.BooksList(repository.getBookList(online, lastId))
                else
                    MainViewState.LocalBooksList(repository.getBookList(online, lastId))
            } catch (e: Exception) {
                MainViewState.Error(e.localizedMessage)
            }
        }
    }

    private fun getSearchBooks(searchTxt: String) {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            _state.value = try {
                MainViewState.SearchBooksList(repository.getSearchBookList(searchTxt))
            } catch (e: Exception) {
                MainViewState.Error(e.localizedMessage)
            }
        }
    }

    private fun getBookContent(bookId: Int) {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            _state.value = try {
                MainViewState.GetBookContent(repository.getBookContent(bookId))
            } catch (e: Exception) {
                MainViewState.Error(e.localizedMessage)
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            _state.value = try {
                MainViewState.Logout(repository.logout())
            } catch (e: Exception) {
                MainViewState.Error(e.localizedMessage)
            }
        }
    }

}
