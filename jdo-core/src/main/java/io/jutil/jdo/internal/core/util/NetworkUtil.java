package io.jutil.jdo.internal.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-08-12
 */
public class NetworkUtil {
    private static Logger logger = LoggerFactory.getLogger(NetworkUtil.class);

	private NetworkUtil() {
	}

    public static byte[] getIpForByteArray() {
        var list = getIpList();
        if (list.isEmpty()) {
            throw new IllegalArgumentException("无法获取IP");
        }
        return list.get(0).getAddress();
    }

    public static String getIpForString() {
        var list = getIpList();
        if (list.isEmpty()) {
            throw new IllegalArgumentException("无法获取IP");
        }
        return list.get(0).getHostAddress();
    }

    public static int getIpForInt() {
        var list = getIpList();
        if (list.isEmpty()) {
            throw new IllegalArgumentException("无法获取IP");
        }
        return list.get(0).hashCode();
    }

    public static List<Inet4Address> getIpList() {
        List<Inet4Address> list = new ArrayList<>();
        try {
            var networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                var networkInterface = networkInterfaces.nextElement();
                var inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    var inetAddress = inetAddresses.nextElement();
                    if (!(inetAddress instanceof Inet4Address)) {
                        continue;
                    }

                    if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
                        continue;
                    }

                    var inet4Address = (Inet4Address) inetAddress;
                    list.add(inet4Address);
                }
            }
        }
        catch (SocketException e) {
            logger.error("Error,", e);
        }
        return list;
    }

}
