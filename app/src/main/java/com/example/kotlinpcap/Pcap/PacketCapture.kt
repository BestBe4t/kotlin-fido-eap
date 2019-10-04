package com.example.kotlinpcap.Pcap

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.jnetpcap.Pcap
import org.jnetpcap.PcapIf
import org.jnetpcap.packet.PcapPacket
import org.jnetpcap.packet.PcapPacketHandler
import java.io.File
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

@Suppress("ConvertToStringTemplate")
class PacketCapture() {
    init {
        try {
            System.loadLibrary(File("jnetpcap.dll").absolutePath)
        } catch (e: UnsatisfiedLinkError) {
            System.exit(1)
        }
    }

    companion object{
        var Tag = "PacketCapture"
        var snaplen = 64*1024
        var flags = Pcap.MODE_PROMISCUOUS
        var time_out = 10*1000
    }

    fun packet_capter(): String{
        var alldevs:List<PcapIf> = ArrayList<PcapIf>()
        val errbuf:StringBuilder = StringBuilder()
        val r = Pcap.findAllDevs(alldevs, errbuf)

        var result:String = ""

        if(r == Pcap.NEXT_EX_NOT_OK || alldevs.isEmpty()){
            Log.d(Tag, "Can't read list of devices, error is " + errbuf.toString())
            return result
        }
        Log.d(Tag, "Network devices found\n")

        var i=1
        for (device in alldevs){
            var description = if(device.description != null) device.description else "No description available"
            Log.d(Tag, "#" + i + ": " + device.name + "[" + description + "]\n")
        }

        var pcap = Pcap.openLive(alldevs[0].name, snaplen, flags, time_out, errbuf)

        if(pcap == null){
            Log.d(Tag, "Error while opening device for capture" + errbuf.toString())
            return result
        }

        val jpacketHandle:PcapPacketHandler<String> = object: PcapPacketHandler<String>{
            override fun nextPacket(packet: PcapPacket, user: String){
                Log.d(Tag, "\nReceived packet at " + Date(packet.captureHeader.timestampInMillis()) + " caplen = " + packet + " len = " + packet.captureHeader.wirelen() + " " + user + "\n")
            }
        }

        pcap.loop(10, jpacketHandle, Tag)
        pcap.close()

        return result
    }
}