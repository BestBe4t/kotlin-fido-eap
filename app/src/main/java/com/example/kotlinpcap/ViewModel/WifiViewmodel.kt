package com.example.kotlinpcap.ViewModel

import android.app.Application
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.kotlinpcap.Auth.State
import com.example.kotlinpcap.Wifi.WifiRepository

class WifiViewmodel(application: Application): AndroidViewModel(application){
    private val wifiRepository = WifiRepository.getInstance(application.applicationContext)
    
    val currentUsername: LiveData<String> =
        Transformations.map(wifiRepository.getState()) { state ->
            when (state) {
                is State.SigningIn -> state.username
                is State.SignedIn -> state.username
                is State.SignedInHascredential -> state.username
                else -> "User"
            }
        }
    
    val wifiList = wifiRepository.getWifiList()
    
    fun wificonnect(ssid: String): WifiConfiguration?{
        for (i in wifiList.value!!){
            if (i.SSID==ssid){
                return wifiRepository.wifiConnect(i)
            }
        }
        return null
    }
    
    fun wifiList(wifiList: MutableList<ScanResult>?){
        wifiRepository.wifiList(wifiList)
    }

    fun setpassword(password:String){
        wifiRepository.rppassword(password)
    }
}