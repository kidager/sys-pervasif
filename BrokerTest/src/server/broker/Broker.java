package server.broker;

import server.event.Event;
import server.event.EventListener;
import server.event.callback.OnEventReception;

public class Broker {

  public void send(String[] args) {
    // My IP : 172.16.14.159
    EventListener eventListener = new EventListener(9000);

    eventListener.setOnEventReception(new OnEventReception() {
      @Override
      public void eventReceived(Event event) {
        System.out.println(event.getName() + " : " + event.getSenderIP());
      }
    });

    System.out.println("-----  Started -----");
    eventListener.run();
    System.out.println("----- Finished -----");
  }
}
