package com.example.Kotlinpcap.ShareResource

sealed class State{
    object SignedOut : State()

    data class SigningIn(
        val username: String
    ): State()

    data class SignInError(
        val error: String
    ) : State()

    data class SignedIn(
        val username: String,
        val token: String
    ) : State()

    data class SignedInHascredential(
        val username: String,
        val token: String,
        val credential: String
    ) : State()
}