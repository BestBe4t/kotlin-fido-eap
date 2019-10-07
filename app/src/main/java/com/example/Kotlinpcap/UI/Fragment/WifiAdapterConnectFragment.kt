package com.example.Kotlinpcap.UI.Fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.Kotlinpcap.R

class WifiAdapterConnectFragment: DialogFragment(){

	companion object{
		private const val SSID = "ssid"
		private const val TAG = "Adapter"

		fun newInstance(wifiid: String) = WifiAdapterConnectFragment().apply {
			arguments = Bundle().apply {
				putString(SSID, wifiid)
			}
			Log.i(TAG, "Wifi Adapter")
		}
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		Log.i(TAG, "Wifi Dialog")
		val Dialog_View = parentFragment!!.layoutInflater.inflate(R.layout.popup_pass, null)
		val Dialog_Text = Dialog_View.findViewById<EditText>(R.id.rppassword)
		val builder = AlertDialog.Builder(requireContext())
		val ssid = arguments?.getString(SSID) ?: throw RuntimeException()
		builder.setView(Dialog_View)
			.setPositiveButton(android.R.string.ok) { _, _ ->
				(parentFragment as Listener).onPasswordConfirmed(Dialog_Text.text.toString(), ssid)
				dismiss()
			}
			.setNegativeButton(android.R.string.cancel) { _, _ ->
				dismiss()
			}
		return builder.create()
	}
	interface Listener{
		fun onPasswordConfirmed(rppassword: String, ssid: String)
	}
}