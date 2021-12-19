package com.church.nagwa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.church.nagwa.data.repository.MainRepository
import com.church.nagwa.ui.intent.MainIntent
import com.church.nagwa.ui.viewState.MainViewState
import com.church.nagwa.util.NetworkHelper
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
                    is MainIntent.GetUserInfo -> getItemList()
                }
            }
        }
    }

    private fun getItemList() {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            _state.value = try {
                MainViewState.GetUserInfo(repository.getItemList())
            } catch (e: Exception) {
                MainViewState.Error(e.localizedMessage)
            }
        }
    }

}
