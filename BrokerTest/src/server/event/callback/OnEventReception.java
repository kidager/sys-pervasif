package server.event.callback;

import server.event.Event;

public interface OnEventReception {

  public void eventReceived(Event event);

}
