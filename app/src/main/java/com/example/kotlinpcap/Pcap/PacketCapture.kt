package com.example.kotlinpcap.Pcap

import com.slytechs.jnetstream.livecapture.AbstractLiveCapture
import com.slytechs.utils.memory.BitBuffer
import org.jnetpcap.Pcap
import org.jnetstream.capture.file.pcap.PcapDLT
import org.jnetpcap.packet.PcapPacketHandler
import org.jnetstream.capture.LiveCaptureDevice
import org.jnetstream.capture.LivePacket
import org.jnetstream.capture.LivePacketFactory
import org.jnetstream.filter.Filter
import org.jnetstream.packet.ProtocolFilterTarget
import org.jnetstream.protocol.ProtocolRegistry
import java.io.IOException
import java.lang.IllegalStateException
import java.lang.StringBuilder
import java.nio.ByteBuffer

@Suppress("ConvertToStringTemplate")
class PacketCapture: AbstractLiveCapture{
    companion object{
        private const val DEFAULT_BREAK_LOOP_CHECK = 1
        private lateinit var pcaps: Array<Pcap?>
    }
    private var factory: LivePacketFactory



    constructor(captureDevice: Array<LiveCaptureDevice>, captureCount: Int, snaplen: Int, promiscuous: Boolean, timeout: Int, filter: Filter<ProtocolFilterTarget>):
            super(captureDevice, captureCount, snaplen, promiscuous, timeout, filter){
        pcaps = arrayOfNulls(captureDevice.size)

        val errbuf = StringBuilder()
        var i = 0

        for(i in 0..captureDevice.size){
            val device = captureDevice[i]
            pcaps[i] = Pcap.openLive(device.name, snaplen, if(promiscuous) 1 else 0, DEFAULT_BREAK_LOOP_CHECK, errbuf)

            if(pcaps[i] == null){
                throw IOException("Unable to open capture device" + device.name + ", Error: " + errbuf.toString())
            }
        }

        this.factory = ProtocolRegistry.getPacketFactory(LivePacketFactory::class.java, "com.slytechs.jnetstream.packet.DefaultLivePacketFactory")
    }

    override fun capture(count: Int, index: Int) {
        if(index < 0 || index >= pcaps.size || pcaps[index] == null){
            throw IllegalStateException("Trying to use an unopen Pcap capture session")
        }

        val dit = pcaps[index]!!.datalink()
        val id = ProtocolRegistry.getProtocolEntry(PcapDLT.asConst(dit.toLong()))

        val handler: PcapPacketHandler<LiveCaptureDevice> = PcapPacketHandler{ _, device ->
            fun nextPacket(seconds: Long, useconds: Int, caplen: Int, len:Int, buffer: ByteBuffer){
                val packet: LivePacket = factory.newLivePacket(id, buffer, BitBuffer.wrap(buffer), seconds, useconds, caplen, len, device)

                dispatch(packet)
            }
        }
        var r:Int
        val ts = System.currentTimeMillis()
        do{
            r = pcaps[index]!!.dispatch(count, handler, devices[index])

            if(r == Pcap.NOT_OK){
                throw IOException(pcaps[index]!!.err)
            }else if(timeout != 0 && System.currentTimeMillis() - ts >= timeout){
                break
            }
        }while (r >= 0)

        pcaps[index]!!.close()
        pcaps[index] = null
    }

    override fun close() {
        var i:Int
        for (i in 0 .. pcaps.size){
            val pcap = pcaps[i]
            if(pcap == null){
                continue
            }

            pcap.breakloop()
        }

        super.close()
    }
}