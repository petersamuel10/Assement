package com.church.ministry.ui.view.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.church.ministry.R
import com.church.ministry.base.BaseActivity
import com.church.ministry.ui.intent.MainIntent
import com.church.ministry.ui.view.MainActivity
import com.church.ministry.ui.viewState.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AuthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        loginBtn.setOnClickListener {
            lifecycleScope.launch {
                authViewModel.userIntent.send(
                    MainIntent.Login(
                        nameInEd.text.toString(),
                        passwordInEd.text.toString()
                    )
                )
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            authViewModel.state.collect {
                when (it) {
                    is MainViewState.Idle -> {

                    }
                    is MainViewState.Loading -> {
                        progressDialog.show()
                    }

                    is MainViewState.Login -> {
                        progressDialog.dismiss()
                        finishAct()
                    }
                    is MainViewState.Error -> {
                        progressDialog.dismiss()
                        Toast.makeText(this@AuthActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun finishAct() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // region variable
    private val authViewModel: AuthViewModel by viewModels()
    //endregion
}