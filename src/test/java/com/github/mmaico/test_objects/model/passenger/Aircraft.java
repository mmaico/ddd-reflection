package com.github.mmaico.test_objects.model.passenger;

import com.github.mmaico.shared.annotations.Attribute;
import com.github.mmaico.shared.annotations.Model;

import java.util.Date;

@Model
public class Aircraft {

  private Long id;

  private Date start;

  @Attribute(destinationName = "seller")
  private Passenger passenger;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Passenger getPassenger() {
    return passenger;
  }

  public void setPassenger(Passenger passenger) {
    this.passenger = passenger;
  }
}
