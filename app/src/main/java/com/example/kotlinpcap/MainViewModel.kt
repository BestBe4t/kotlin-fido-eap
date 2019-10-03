package com.example.kotlinpcap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kotlinpcap.Auth.AuthRepository

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val repository = AuthRepository

}