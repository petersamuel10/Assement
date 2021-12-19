package com.church.nagwa.ui.viewState

import com.church.nagwa.data.model.ItemInfo

sealed class MainViewState {
    object Idle : MainViewState()
    object Loading : MainViewState()
    data class GetUserInfo(val itemList: List<ItemInfo>) : MainViewState()
    data class Error(val error: String?) : MainViewState()
}
