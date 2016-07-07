package com.trex.clone.objects.ddd.passenger;



import com.trex.DocumentExtractor;
import com.trex.clone.objects.hibernate_entities.User;
import com.trex.shared.annotations.EntityReference;
import com.trex.shared.annotations.Extractor;

@EntityReference(User.class)
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
