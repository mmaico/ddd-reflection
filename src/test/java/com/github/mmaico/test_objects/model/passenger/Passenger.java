package com.github.mmaico.test_objects.model.passenger;



import com.github.mmaico.DocumentConverter;
import com.github.mmaico.shared.annotations.Attribute;
import com.github.mmaico.shared.annotations.Model;

@Model
public class Passenger {

  private Long id;

  private String name;

  @Attribute(destinationName = "documents", converter = DocumentConverter.class)
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


  public String getPassport() {
    return passport;
  }



}
