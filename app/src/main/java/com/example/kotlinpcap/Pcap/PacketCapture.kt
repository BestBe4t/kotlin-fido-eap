package com.example.kotlinpcap.Pcap

import org.jnetpcap.Pcap
import org.jnetpcap.PcapIf
import java.io.File
import java.lang.StringBuilder

class PacketCapture {
    init {
        try {
            System.loadLibrary(File("jnetpcap.dll").absolutePath)
        } catch (e: UnsatisfiedLinkError) {
            System.exit(1)
        }
    }

    fun main(): String{
        var alldevs:List<PcapIf> = ArrayList<PcapIf>()
        val errbuf:StringBuilder = StringBuilder()
        val r = Pcap.findAllDevs(alldevs, errbuf)

        var result:String = ""

        if(r == Pcap.NEXT_EX_NOT_OK || alldevs.isEmpty()){
            result = "Can't read list of devices, error is " + errbuf.toString()
            return result
        }
        result += "Network devices found\n"
        return result
    }
}