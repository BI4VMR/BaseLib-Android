package net.bi4vmr.gradle.util

import java.net.InetSocketAddress
import java.net.Socket

/**
 * 网络相关工具。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object NetUtil {

    fun scanByTCP(ip: String, port: Int): Boolean {
        val socket = Socket()
        try {
            socket.connect(InetSocketAddress(ip, port), 500)
            println("TCP connect test success! IP:[$ip] Port:[$port]")
            return true
        } catch (e: Exception) {
            System.err.println("TCP connect test failed! IP:[$ip] Port:[$port] Reason:[${e.message}]")
            return false
        } finally {
            socket.close()
        }
    }
}
