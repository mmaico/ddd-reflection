package com.trex.test_objects.model.passenger;



import com.trex.DocumentExtractor;
import com.trex.shared.annotations.Model;
import com.trex.test_objects.hibernate_entities.User;
import com.trex.shared.annotations.EntityReference;
import com.trex.shared.annotations.Extractor;

@Model
public class Passenger {

  private Long id;

  private String name;

  private String passport;

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

  @Extractor(DocumentExtractor.class)
  public String getPassport() {
    return passport;
  }



}
