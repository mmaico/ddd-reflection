package com.trex.clone.objects.ddd.passenger;

import com.trex.clone.objects.hibernate_entities.BusinessProposal;
import com.trex.clone.objects.hibernate_entities.User;
import com.trex.shared.annotations.EntityReference;

import java.util.Date;

@EntityReference(BusinessProposal.class)
public class Travel {

  private Long id;

  private Date start;

  @EntityReference(value = User.class, fieldName = "seller")
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
