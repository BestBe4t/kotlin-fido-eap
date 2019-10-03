package com.example.kotlinpcap.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.kotlinpcap.ViewModel.LoginViewmodel
import com.example.kotlinpcap.databinding.LoginFragmentBinding

class LoginFragment: Fragment() {

    companion object{
        private const val Tag = "Login"
    }

    private lateinit var viewModel:LoginViewmodel
    private lateinit var binding:LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel  = ViewModelProviders.of(this).get(LoginViewmodel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}