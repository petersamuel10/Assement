package com.church.ministry.ui.intent

sealed
class MainIntent {

    class Login(val userName: String, val password: String) : MainIntent()
    object Logout : MainIntent()
    object GetUserInfo : MainIntent()
    class CheckNewBooks(val lastId: Int) : MainIntent()
    object GetLocalBooksList : MainIntent()
    class GetBooksList(val lastId: Int) : MainIntent()
    class GetSearchBooks(val searchTxt: String) : MainIntent()
    class GetBookContent(val bookId: Int) : MainIntent()
    //  object FetchUser : MainIntent()

}