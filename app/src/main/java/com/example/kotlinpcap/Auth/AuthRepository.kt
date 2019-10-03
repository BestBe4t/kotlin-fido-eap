package com.example.kotlinpcap.Auth

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AuthRepository(
    private val prefs: SharedPreferences,
    private val executor: Executor
) {
    companion object{
        private val PREF ="pref"
        private val PREF_USER_NAME = "username"
        private val PREF_TOKEN = "token"
        private val PREF_CREDENTIALID = "credentialid"

        private var instance: AuthRepository? = null

        fun getInstance(context: Context): AuthRepository{
            return instance ?: synchronized(this){
                instance ?: AuthRepository(
                    context.getSharedPreferences(PREF, Context.MODE_PRIVATE),
                    Executors.newFixedThreadPool(64)
                ).also { instance = it }
            }
        }
    }

    private val StateListeners = mutableListOf<(State) -> Unit>()

    fun getState():LiveData<State>{
        return object : LiveData<State>(){
            private val listener = {state: State->
                postValue(state)
            }

            init{
                val username = prefs.getString(PREF_USER_NAME, null)
                val token = prefs.getString(PREF_TOKEN, null)
                val credentialId = prefs.getString(PREF_CREDENTIALID, null)
                value = when{
                    username.isNullOrBlank() -> State.SignedOut
                    token.isNullOrBlank() -> State.SigningIn(username)
                    credentialId.isNullOrBlank() -> State.SignedIn(username, token)
                    else -> State.SignedInHascredential(username, token, credentialId)
                }
            }

            override fun onActive() {
                StateListeners.add(listener)
            }

            override fun onInactive() {
                StateListeners.remove(listener)
            }
        }
    }
}