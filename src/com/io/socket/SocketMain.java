package com.io.socket;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * @author lubaijiang
 */
public class SocketMain {
    /**
     * 网络设备接口
     * @param args
     * @throws SocketException
     */
    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()){
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            //1. 获取因特网地址：一对多：一个网络设备对多个因特网地址
            List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
            for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                InetAddress address = interfaceAddress.getAddress();
                InetAddress broadcast = interfaceAddress.getBroadcast();
                short networkPrefixLength = interfaceAddress.getNetworkPrefixLength();
            }
            //2. 获取IP地址，一对多：一个网络设备对多个IP地址
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()){
                InetAddress inetAddress = inetAddresses.nextElement();
                String hostName = inetAddress.getHostName();
                String hostAddress = inetAddress.getHostAddress();
                byte[] address = inetAddress.getAddress();
                String canonicalHostName = inetAddress.getCanonicalHostName();
            }
            //3. 获取一些硬件信息，常用的获取名称，mac地址，最大传输单元等
            //名称
            String name = networkInterface.getName();
            //mac地址
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            //最大传输单元
            int mtu = networkInterface.getMTU();
            //是否为localhost回调/回环接口
            boolean loopback = networkInterface.isLoopback();
            //是否为点对点设备
            boolean pointToPoint = networkInterface.isPointToPoint();
            //是否支持组播
            boolean supportsMulticast = networkInterface.supportsMulticast();
        }

    }
    /**
     * 几个重要的概念：
     * 1. localhost回调/回环
     * 2. 多播/组播
     */
}
