package com.example.kotlinpcap.Wifi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinpcap.Wifi.WifiItem
import com.example.kotlinpcap.databinding.WifiItemBinding

class WifiAdapter(
        private val onWifiClicked: (String) -> Unit
): ListAdapter<WifiItem, FIDOViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FIDOViewHolder {
        return FIDOViewHolder(
                WifiItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                ),
                onWifiClicked
        )
    }

    override fun onBindViewHolder(holder: FIDOViewHolder, position: Int) {
        holder.binding.wifiitem = getItem(position)
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WifiItem>(){
    override fun areContentsTheSame(oldItem: WifiItem, newItem: WifiItem): Boolean {
        return oldItem.SSID == newItem.SSID
    }

    override fun areItemsTheSame(oldItem: WifiItem, newItem: WifiItem): Boolean {
        return oldItem == newItem
    }
}

class FIDOViewHolder(val binding: WifiItemBinding, onWifiClicked: (String) -> Unit):
        RecyclerView.ViewHolder(binding.root){
    init {
        binding.ssid.setOnClickListener {
            binding.wifiitem?.let { c ->
                onWifiClicked(c.SSID)
            }
        }
    }
}