package utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
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
        interfaces.add(netInterface);
      }

    } catch (SocketException ex) {
      ex.printStackTrace();
    }

    return interfaces;
  }

  public static InetAddress getMyIP() {
    Enumeration<NetworkInterface> netInterfaces = null;
    NetworkInterface ni = null;

    try {
      netInterfaces = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException ex) {}

    if (netInterfaces != null && netInterfaces.hasMoreElements()) {
      ni = netInterfaces.nextElement();
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

  public static InetAddress getCurrentEnvironmentNetworkIp() {
    InetAddress currentHostIpAddress = null;
    Enumeration<NetworkInterface> netInterfaces = null;
    try {
      netInterfaces = NetworkInterface.getNetworkInterfaces();

      while (netInterfaces.hasMoreElements()) {
        NetworkInterface ni = netInterfaces.nextElement();

      }
      if (currentHostIpAddress == null) {

      }
    } catch (Exception e) {
      currentHostIpAddress = null;
    }
    return currentHostIpAddress;
  }

}
