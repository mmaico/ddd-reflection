package com.trex.test_objects.model.negotiation;


import com.trex.shared.annotations.Model;

@Model
public class Product extends AbstractModel {

  private Long id;

  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
