package com.example.Kotlinpcap.UI.ViewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.Kotlinpcap.Auth.AuthRepository
import com.example.Kotlinpcap.ShareResource.State

class LoginViewmodel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository.getInstance(application)

    private val _sending = MutableLiveData<Boolean>()
    val sending: LiveData<Boolean>
        get() = _sending

    val userName = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()

    val nextEnabled = MediatorLiveData<Boolean>().apply {
        var sendingValue = _sending.value ?: false
        var usernameValue = userName.value
        var userpasswordValue = userPassword.value
        fun update() {
            value = !sendingValue && !usernameValue.isNullOrBlank() && !userpasswordValue.isNullOrBlank()
        }
        addSource(_sending) {
            sendingValue = it
            update()
        }
        addSource(userName) {
            usernameValue = it
            update()
        }
        addSource(userPassword) {
            usernameValue = it
            update()
        }
    }

    val currentUser:LiveData<String> =
        Transformations.map(repository.getState()) { state ->
            when (state){
                is State.SigningIn -> state.username
                is State.SignedIn -> state.username
                else -> "User"
            }
        }

    fun Login(){
        val userName = userName.value
        val userPassword = userPassword.value
        if(!userName.isNullOrBlank() && !userPassword.isNullOrBlank())
            repository.Login(userName, userPassword, _sending)
    }
}