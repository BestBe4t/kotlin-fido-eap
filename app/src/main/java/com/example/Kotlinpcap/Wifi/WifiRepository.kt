package com.example.Kotlinpcap.Wifi

import android.content.Context
import android.content.SharedPreferences
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.Kotlinpcap.ShareResource.State
import com.example.Kotlinpcap.ShareResource.liveStringSet
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class WifiRepository (
    private val api: WifiApi,
    private val prefs: SharedPreferences,
    private val executor: Executor
){
    companion object {
        private const val Tag = "WifiRepository"

        // Keys for SharedPreferences
        private const val PREFS_NAME = "auth"
        private const val PREF_USERNAME = "username"
        private const val PREF_TOKEN = "token"
        private const val PREF_RPPASSWORD = "rppassword"
        private const val PREF_CREDENTIALS = "credentials"
        private const val PREF_LOCAL_CREDENTIAL_ID = "local_credential_id"
        private const val PREF_WIFILIST = "wifilist"
        private const val PREF_LEVEL= "level"

        private var instance: WifiRepository? = null

        fun getInstance(context: Context): WifiRepository {
            return instance ?: synchronized(this) {
                instance ?: WifiRepository(
                    WifiApi(context),
                    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE),
                    Executors.newFixedThreadPool(64)
                ).also { instance = it }
            }
        }
    }

    fun getState(): LiveData<State> {
        return object : LiveData<State>() {

            private val listener = { state: State ->
                postValue(state)
            }

            init {
                val username = prefs.getString(PREF_USERNAME, null)
                val token = prefs.getString(PREF_TOKEN, null)
                val credentialID = prefs.getString(PREF_LOCAL_CREDENTIAL_ID, null)
                value = when {
                    username.isNullOrBlank() -> State.SignedOut
                    token.isNullOrBlank() -> State.SigningIn(username)
                    credentialID.isNullOrBlank() -> State.SignedIn(username, token)
                    else -> State.SignedInHascredential(username, token, credentialID)
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

    private val StateListeners = mutableListOf<(State) -> Unit>()


    private fun invokeStateListeners(state: State) {
        val listeners = StateListeners.toList() // Copy
        for (listener in listeners) {
            listener(state)
        }
    }

    fun wifiList(scanResult: MutableList<ScanResult>?) {
        var wifiItem= mutableListOf<WifiItem>()

        for (i in scanResult!!) {
            wifiItem.add(WifiItem(i.SSID, i.capabilities, i.level))
        }
        prefs.edit(commit = true) {
            putStringSet(PREF_WIFILIST, wifiItem.toStringSetWifilist())
        }
    }

    /**
     * WifiItem refresh & parse
     */
    fun getWifiList(): LiveData<List<WifiItem>> {
        return Transformations.map(prefs.liveStringSet(PREF_WIFILIST, emptySet())) { set ->
            parseWifiList(set)
        }
    }

    private fun List<WifiItem?>.toStringSetWifilist(): Set<String> {
        return mapIndexed { index, wifilist ->
            "$index;${wifilist!!.SSID};${wifilist.Capability};${wifilist.Level}"
        }.toSet()
    }

    private fun parseWifiList(set: Set<String>?): List<WifiItem> {
        return set!!.map { s ->
            val (index, SSID, capability, Level) = s.split(";")
            index to WifiItem(SSID, capability, Level.toInt())
        }.sortedBy { (index, _) -> index }
            .map { (_, WifiList) -> WifiList }
    }

    fun wifiCheck(wifi: WifiItem): Int{
        return api.wifiCheck(wifi)
    }

    fun wifiConnect(wifi: WifiItem): WifiConfiguration{
        return api.wifiConnectDownAPI28(wifi, prefs)
    }

    fun rppassword(password: String){
        prefs.edit(commit = true) {
            putString(PREF_RPPASSWORD, password)
        }
    }

    fun research(){
        executor.execute {
            prefs.edit(commit = true){
                remove(PREF_WIFILIST)
            }
        }
    }
}