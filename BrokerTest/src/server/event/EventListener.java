package server.event;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import common.config.Configuration;

import server.event.callback.OnEventReception;

public class EventListener implements Runnable {

  private int               portNumber;

  private ServerSocket      serverSocket;
  private Socket            socket;
  private ObjectInputStream ois;
  private Event             eventReceived;
  private boolean           _continue = true;

  private OnEventReception  onEventReception;

  public EventListener(int portNumber) {
    this.portNumber = portNumber;
  }

  public void setOnEventReception(OnEventReception onEventReception) {
    this.onEventReception = onEventReception;
  }

  public void terminate() {
    _continue = false;
  }

  @Override
  public void run() {
    try {
      // Init socket
      serverSocket = new ServerSocket(portNumber);
      serverSocket.setSoTimeout(Configuration.SOCKET_WAIT_TIMEOUT);
      // Listen
      while (_continue) {
        try {
          socket = serverSocket.accept();
          ois = new ObjectInputStream(socket.getInputStream());
          while (null != (eventReceived = (Event) ois.readObject())) {
            onEventReception.eventReceived(eventReceived);
          }
        } catch (EOFException | SocketTimeoutException ex) {
          ex.printStackTrace();
        }
      }
      socket.close();
    } catch (IOException | ClassNotFoundException ex) {
      ex.printStackTrace();
    }

  }

}
