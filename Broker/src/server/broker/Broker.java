package server.broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import server.event.callback.OnConsume;
import server.model.Consumer;
import server.model.Producer;
import server.model.Product;
import server.model.ProductRequest;
import utils.NetworkUtils;
import common.config.Configuration;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Broker {

  private static List<Product>                   productList = new ArrayList<Product>();
  private static HashMap<String, ProductRequest> requestList = new HashMap<String, ProductRequest>();
  private static Consumer                        consumer;
  private static Producer<Product>               producer;

  public static void main(String[] args) throws Exception {
    System.out.println("------------------------------------------");
    System.out.println("----------   Server  Started   -----------");
    System.out.println("------------------------------------------");
    System.out.println("IP : " + NetworkUtils.getMyIP());
    System.out.println("------------------------------------------");

    Thread c = new Thread(new Runnable() {
      @Override
      public void run() {
        // Listen to port
        consumer = new Consumer(Configuration.BROKER_RECEIVE_PORT);
        consumer.setOnConsume(new OnConsume() {
          @Override
          public void onConsume(Object objectReceived, String ipSource) {
            // Got object, treat it
            try {
              if (objectReceived instanceof Product) {
                // Product
                Product p = (Product) objectReceived;
                productList.add(p);
                System.out.println("[" + ipSource + "] Receive product : " + p.getCategory());
              } else if (objectReceived instanceof ProductRequest) {
                // Product request
                ProductRequest pr = (ProductRequest) objectReceived;
                requestList.put(ipSource, pr);
                System.out.println("[" + ipSource + "] Received request : " + pr.getCategory());
              }
              // Match producers and consumers
              matchProducerCosumer();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        });
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        try {
          consumerThread.join();
        } catch (InterruptedException ex) {
          System.out.println("ERROR");
        }
      }
    });
    c.start();
  }

  public static boolean matchProducerCosumer() {
    for (Entry<String, ProductRequest> pr : requestList.entrySet()) {
      for (Product p : productList) {
        if (p.getCategory().toLowerCase().equals(pr.getValue().getCategory().toLowerCase())) {
          // PRODUCE
          producer = new Producer<Product>(pr.getKey(), Configuration.CLIENT_RECEIVE_PORT, p);
          new Thread(producer).start();
          requestList.remove(pr);
          //productList.remove(p);
          //return true;
        }
      }
    }
    return false;
  }
}
