package server.event.callback;

import java.net.InetAddress;
import java.util.List;

public interface OnNetworkScanFinished {

  public void onNetworkScanFinished(List<InetAddress> addressesList);

}
