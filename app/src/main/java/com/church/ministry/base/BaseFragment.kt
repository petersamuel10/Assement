package com.church.ministry.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.church.ministry.util.ProgressDialog

open class BaseFragment : Fragment() {

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(requireActivity())
    }

}