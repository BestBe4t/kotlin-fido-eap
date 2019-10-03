package com.example.kotlinpcap.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.kotlinpcap.Auth.AuthRepository
import com.example.kotlinpcap.Auth.State

class LoginViewmodel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository.getInstance(application)

    val currentUser:LiveData<String> =
        Transformations.map(repository.getState()) { state ->
            when (state){
                is State.SigningIn -> state.username
                is State.SignedIn -> state.username
                else -> "User"
            }
        }

    val signedInEnabled = MediatorLiveData<Boolean>().apply {
        fun update(processing: Boolean, password: String){
            value = !processing && password.isNotBlank()
        }
    }

    fun login(){

    }
}