package com.example.Kotlinpcap.UI.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.Kotlinpcap.UI.ViewModel.LoginViewmodel
import com.example.Kotlinpcap.databinding.LoginFragmentBinding

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
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.inputUserid.setOnEditorActionListener { _, actionid, _ ->
            if(actionid == EditorInfo.IME_ACTION_GO){
                if(!binding.inputPassword.text.isNullOrBlank()) {
                    viewModel.Login()
                    true
                }else{
                    false
                }
            }else{
                false
            }
        }

        binding.inputPassword.setOnEditorActionListener { _, actionid, _ ->
            if(actionid == EditorInfo.IME_ACTION_GO){
                if(!binding.inputUserid.text.isNullOrBlank()) {
                    viewModel.Login()
                    true
                }else{
                    false
                }
            }else{
                false
            }
        }
    }
}