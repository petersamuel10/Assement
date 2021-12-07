package com.church.ministry.ui.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.church.ministry.data.repository.MainRepository
import com.church.ministry.ui.intent.MainIntent
import com.church.ministry.ui.viewState.MainViewState
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
class AuthViewModel @Inject constructor(
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
                    is MainIntent.Login -> login(it.userName, it.password)
                    is MainIntent.GetUserInfo -> getUserInfo()
                }
            }
        }
    }

    private fun login(userName: String, password: String) {
        viewModelScope.launch {
            _state.value = MainViewState.Loading
            _state.value = try {
                MainViewState.Login(repository.login(userName, password))
            } catch (e: Exception) {
                MainViewState.Error(e.localizedMessage)
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

}