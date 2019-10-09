package com.example.kotlinpcap.Wifi

import android.content.Context
import android.content.SharedPreferences
import android.net.wifi.WifiConfiguration

class WifiApi(context: Context){
    private val context = context.applicationContext

    /**
     * Checked about this WIFI is Open type or using password type
     */
    fun wifiCheck( ScanResult: WifiItem ): Int{
        val capabilities:String = ScanResult.Capability
        val IS_FIDO_WIFI = 1
        val IS_WPA_WIFI = 2
        val IS_OPEN_WIFI = 3

        if(capabilities.contains("FIDO")){
            return IS_FIDO_WIFI
        }else if(capabilities.contains("WPA2")){
            return IS_WPA_WIFI
        }else{
            return IS_OPEN_WIFI
        }
    }

    /**
     * Connected WIFI(API Version under 28)
     */
    fun wifiConnectDownAPI28(ScanResult: WifiItem, perfs:SharedPreferences): WifiConfiguration{
        val WIFI_Con = WifiConfiguration()
        val capabilities:String = ScanResult.Capability

        WIFI_Con.SSID = "\""+ScanResult.SSID+"\""
        WIFI_Con.status = WifiConfiguration.Status.ENABLED
        WIFI_Con.allowedProtocols.set(WifiConfiguration.Protocol.RSN)

        if (capabilities.contains("PSK"))
            WIFI_Con.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)

        if (capabilities.contains("EAP"))
            WIFI_Con.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP)

        if (capabilities.contains("CCMP")) {
            WIFI_Con.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
            WIFI_Con.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
        }
        if (capabilities.contains("TKIP")) {
            WIFI_Con.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
        }
        WIFI_Con.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)

        if(!perfs.getString("rppassword", null).isNullOrEmpty()){   //FIDO or WAP
            WIFI_Con.preSharedKey = "\""+perfs.getString("rppassword", null)+"\""
        }else{
            WIFI_Con.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
        }

        return WIFI_Con
    }

    /**
     * Connected WIFI(API Version 29)
    fun wifiConnectUPAPI29(ScanResult: WifiItem, perfs:SharedPreferences): Boolean{
        val WIFI_CONNECT_SUCCESS = true
        val WIFI_CONNECT_FAIL = false
        val NetworkID = 1

        if (NetworkID != -1){
            return WIFI_CONNECT_SUCCESS     //true
        }else{
            return WIFI_CONNECT_FAIL        //false
        }
    }
    */
}