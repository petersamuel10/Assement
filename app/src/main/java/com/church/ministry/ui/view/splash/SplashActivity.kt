package com.church.ministry.ui.view.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.church.ministry.R
import com.church.ministry.base.BaseActivity
import com.church.ministry.ui.intent.MainIntent
import com.church.ministry.ui.view.MainActivity
import com.church.ministry.ui.view.auth.AuthActivity
import com.church.ministry.ui.view.auth.AuthViewModel
import com.church.ministry.ui.viewState.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SplashActivity : BaseActivity() {

    private val mainViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        ic_splash.animate()
            .setDuration(7000)
            .withEndAction {
                lifecycleScope.launch {
                    mainViewModel.userIntent.send(MainIntent.GetUserInfo)
                }
            }
            .start()

        observeViewModel()

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainViewState.GetUserInfo -> {
                        finishAct(it.user.isLogged)
                    }
                    is MainViewState.Error -> {
                        Toast.makeText(this@SplashActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun finishAct(logged: Boolean) {
        startActivity(
            Intent(
                this,
                if (logged) MainActivity::class.java else AuthActivity::class.java
            )
        )
        finish()
    }
}