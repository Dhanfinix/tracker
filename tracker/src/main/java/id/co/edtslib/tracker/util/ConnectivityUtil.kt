package id.co.edtslib.tracker.util

import java.net.NetworkInterface
import java.util.*

object ConnectivityUtil {
    fun getIPAddress(useIPv4: Boolean): String? {
        try {

            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            for (networkInterface in networkInterfaces) {
                for(address in networkInterface.inetAddresses) {
                    if (! address.isAnyLocalAddress) {
                        val isIPv4 = address.hostAddress != null &&
                                address.hostAddress!!.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return address.hostAddress
                        }
                        else {
                            if (! isIPv4) {
                                val delim = if (address.hostAddress == null) -1 else
                                    address.hostAddress!!.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) address.hostAddress?.uppercase(Locale.getDefault()) else address.hostAddress?.substring(
                                    0,
                                    delim
                                )?.uppercase(Locale.getDefault())
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        } // for now eat exceptions
        catch (ignored: Error) {
        } // for now eat exceptions
        return ""
    }
}