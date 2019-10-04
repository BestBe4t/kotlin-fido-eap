package com.example.kotlinpcap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kotlinpcap.Auth.State
import com.example.kotlinpcap.UI.LoginFragment
import com.example.kotlinpcap.UI.WifiFragment

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)

        viewModel.State.observe(this, Observer { state ->
            when (state){
                is State.SignedOut -> {
                    showFragment(LoginFragment::class.java){
                        LoginFragment()
                    }
                }
                is State.SignedIn -> {
                    showFragment(WifiFragment::class.java){
                        WifiFragment()
                    }
                }
                is State.SigningIn -> {
                    showFragment(WifiFragment::class.java){
                        WifiFragment()
                    }
                }
                is State.SignedInHascredential -> {

                }
                is State.SignInError ->{
                    Toast.makeText(this, state.error, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun showFragment(clazz: Class<out Fragment>, create: () -> Fragment) {
        val manager = supportFragmentManager
        if (!clazz.isInstance(manager.findFragmentById(R.id.container))) {
            manager.commit {
                replace(R.id.container, create())
            }
        }
    }
}
