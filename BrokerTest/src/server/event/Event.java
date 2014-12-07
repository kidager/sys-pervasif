package server.event;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Event implements Serializable {

  private String name;
  private String type;
  private String content;
  private String senderIP;

  public Event(String name, String type, String content, String senderIP) {
    this.name = name;
    this.type = type;
    this.content = content;
    this.senderIP = senderIP;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSenderIP() {
    return senderIP;
  }

  public void setSenderIP(String senderIP) {
    this.senderIP = senderIP;
  }

}
