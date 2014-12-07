package server.event;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class EventSender implements Runnable {

  private String sendToAddress = "0.0.0.0";
  private int    sendToPort    = 0;
  private Event  eventToSend;

  public EventSender(String sendToAddress, int sendToPort, Event eventToSend) {
    this.sendToAddress = sendToAddress;
    this.sendToPort = sendToPort;
    this.eventToSend = eventToSend;
  }

  @Override
  public void run() {
    try {
      Socket socket = new Socket(sendToAddress, sendToPort);
      ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
      oos.writeObject(eventToSend);
      oos.flush();
      oos.close();
      socket.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

}
