package com.example.Kotlinpcap.UI.Fragment

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.Kotlinpcap.UI.ViewModel.WifiViewmodel
import com.example.Kotlinpcap.Wifi.WifiAdapter
import com.example.Kotlinpcap.databinding.WifiFragmentBinding

class WifiFragment:Fragment(), WifiAdapterConnectFragment.Listener {
    companion object{
        private const val Tag = "Wifi"
        private const val FRAGMENT_WIFI_CONNECT = "wifi_connect"
    }

    private lateinit var viewModel: WifiViewmodel
    private lateinit var binding: WifiFragmentBinding
    private lateinit var Wifi_Man: WifiManager
    private lateinit var wifiAdapter: WifiAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(WifiViewmodel::class.java)
        binding = WifiFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.wifiList(wifiScan())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        wifiAdapter = WifiAdapter{SSID ->
            WifiAdapterConnectFragment.newInstance(SSID).show(childFragmentManager,
                FRAGMENT_WIFI_CONNECT
            )
            viewModel

        }
    }

    override fun onPasswordConfirmed(rppassword: String, ssid: String) {
        viewModel.setpassword(rppassword)
        Wifi_Man = context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val test = viewModel.wificonnect(ssid)
        val NetWorkID = Wifi_Man.addNetwork(test)
        Wifi_Man.disconnect()
        Wifi_Man.enableNetwork(NetWorkID, true)
        Wifi_Man.reconnect()
        if (NetWorkID!=-1){
            Toast.makeText(context, "WifiConnected!", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "WifiConnected fail", Toast.LENGTH_SHORT).show()
        }
    }

    fun wifiScan():MutableList<ScanResult>?{
        Wifi_Man = context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return Wifi_Man.scanResults
    }
}