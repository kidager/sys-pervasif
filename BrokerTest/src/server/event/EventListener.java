package server.event;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import server.event.callback.OnEventReception;

public class EventListener implements Runnable {

  private int               portNumber;

  private ServerSocket      serverSocket;
  private Socket            socket;
  private ObjectInputStream ois;
  private Event             eventReceived;

  private OnEventReception  onEventReception;

  public EventListener(int portNumber) {
    this.portNumber = portNumber;
  }

  public void setOnEventReception(OnEventReception onEventReception) {
    this.onEventReception = onEventReception;
  }

  @Override
  public void run() {
    try {
      // Init socket
      serverSocket = new ServerSocket(portNumber);
      socket = serverSocket.accept();

      // Init reader
      ois = new ObjectInputStream(socket.getInputStream());

      // Listen
      while (socket.isBound()) {
        try {
          while (null != (eventReceived = (Event) ois.readObject())) {
            onEventReception.eventReceived(eventReceived);
            // System.out.println(eventReceived.getSenderIP());
          }
        } catch (EOFException ex) {}
      }
      socket.close();
    } catch (IOException | ClassNotFoundException ex) {
      ex.printStackTrace();
    }

  }

}
