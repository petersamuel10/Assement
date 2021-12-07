package com.church.ministry.ui.view.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.church.ministry.MainViewModel
import com.church.ministry.R
import com.church.ministry.ui.intent.MainIntent
import com.church.ministry.ui.view.auth.AuthActivity
import com.church.ministry.ui.viewState.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            mainViewModel.userIntent.send(MainIntent.GetUserInfo)
        }

        observeViewModel()

        logoutBtn.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.Logout)
            }
        }

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainViewState.Idle -> {

                    }
                    is MainViewState.Loading -> {
                        //  authProgressBar.visibility = View.VISIBLE
                    }
                    is MainViewState.GetUserInfo -> {
                        // authProgressBar.visibility = View.GONE
                        name.text = it.user.name

//                        Glide.with(requireContext())
//                            .load(it.user.avatar)
//                            .into(profile_image)
                    }
                    is MainViewState.Logout -> {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                    is MainViewState.Error -> {
                        //   authProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // region variable
    private val mainViewModel: MainViewModel by viewModels()
    //endregion
}