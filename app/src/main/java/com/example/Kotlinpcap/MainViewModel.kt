package com.example.Kotlinpcap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.Kotlinpcap.Auth.AuthRepository

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val repository = AuthRepository.getInstance(application)

    val State = repository.getState()

}