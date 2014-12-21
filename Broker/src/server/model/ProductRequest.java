package server.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductRequest implements Serializable {

  private String category;

  public ProductRequest() {}

  public ProductRequest(String category) {
    this.category = category;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

}
