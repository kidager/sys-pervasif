package server.event.callback;

public interface OnConsume<T> {

  public void onConsume(T product, String sourceIP);

}
