package utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {

  public static List<NetworkInterface> getNetworkInterfacesNames() {
    List<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();

    try {
      Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();

      while (list.hasMoreElements()) {
        NetworkInterface netInterface = list.nextElement();
        if (!netInterface.isLoopback())
          interfaces.add(netInterface);
      }

    } catch (SocketException ex) {
      ex.printStackTrace();
    }

    return interfaces;
  }

  public static InetAddress getMyIP() {
    List<NetworkInterface> netInterfaces = getNetworkInterfacesNames();
    NetworkInterface ni = null;

    if (netInterfaces != null && netInterfaces.size() != 0) {
      ni = netInterfaces.get(0);
    }

    return getMyIP(ni);
  }

  public static InetAddress getMyIP(NetworkInterface _interface) {
    if (_interface == null)
      return null;

    Enumeration<InetAddress> address = _interface.getInetAddresses();
    while (address.hasMoreElements()) {
      InetAddress addr = address.nextElement();
      if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress() && !(addr.getHostAddress().indexOf(":") > -1)) {
        return addr;
      }
    }

    return null;
  }

  public static List<InetAddress> scanConnectedPeers(NetworkInterface _interface) throws InterruptedException {
    // This code assumes IPv4 is used
    final List<InetAddress> list = new ArrayList<InetAddress>();
    final InetAddress localhost = getMyIP(_interface);
    final byte[] ip = localhost.getAddress();

    for (int i = 1; i <= 254; i++) {
      for (int j = 1; j <= 254; j++) {
        ip[2] = (byte) i;
        ip[3] = (byte) j;

        try {
          if (!isInSubmask(InetAddress.getByAddress(ip), getNetworkInterfacesNames().get(0).getInterfaceAddresses()
              .get(0)
              .getBroadcast())) {
            return list;
          }
        } catch (UnknownHostException ex) {
          ex.printStackTrace();
        }

        Thread t = new Thread(new Runnable() {
          public void run() {
            try {
              InetAddress address = InetAddress.getByAddress(ip);
              if (!address.getHostAddress().equals(localhost.getHostAddress()) && address.isReachable(100)) {
                list.add(address);
              }
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }
        });
        t.start();
        t.join();
      }
    }
    return list;
  }

  public static boolean isInSubmask(InetAddress ipAddress, InetAddress subnet) {
    byte[] ipBytes = ipAddress.getAddress();
    byte[] subBytes = subnet.getAddress();

    boolean octet1 = (ipBytes[0] & subBytes[0] & 0xFF) == (ipBytes[0] & 0xFF);
    boolean octet2 = (ipBytes[1] & subBytes[1] & 0xFF) == (ipBytes[1] & 0xFF);
    boolean octet3 = (ipBytes[2] & subBytes[2] & 0xFF) == (ipBytes[2] & 0xFF);
    boolean octet4 = (ipBytes[3] & subBytes[3] & 0xFF) == (ipBytes[3] & 0xFF);
    return (octet1 && octet2 && octet3 && octet4);
  }

}
