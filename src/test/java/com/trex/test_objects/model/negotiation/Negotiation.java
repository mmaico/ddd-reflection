package com.trex.test_objects.model.negotiation;



import com.trex.NegotiationStatusEnumConverter;
import com.trex.shared.annotations.*;
import com.trex.test_objects.hibernate_entities.*;
import com.trex.test_objects.model.customer.Customer;
import com.trex.test_objects.model.seller.Seller;

import java.util.List;

@Model
public class Negotiation extends AbstractModel {

  private Long id;

  private String introduction;

  private String careOf;


  private Seller seller;

  @Attribute(destinationName = "client")
  private Customer customer;

  @Attribute(destinationName = "saleableItems")
  private List<NegotiationItem> items;

  @Attribute(converter = NegotiationStatusEnumConverter.class, destinationName = "temperature")
  private NegotiationStatus status;

  private AdditionalInformation information;



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Seller getSeller() {
    return seller;
  }

  public void setSeller(Seller seller) {
    this.seller = seller;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public List<NegotiationItem> getItems() {
    return items;
  }

  public void setItems(List<NegotiationItem> items) {
    this.items = items;
  }

  public NegotiationStatus getStatus() {
    return status;
  }

  public void setStatus(NegotiationStatus status) {
    this.status = status;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public String getCareOf() {
    return careOf;
  }

  public void setCareOf(String careOf) {
    this.careOf = careOf;
  }

  public AdditionalInformation getInformation() {
    return information;
  }

  public void setInformation(AdditionalInformation information) {
    this.information = information;
  }


  public void changeStatusTo(NegotiationStatus newStatus) {
    if (!this.getStatus().equals(newStatus)) {
        this.setStatus(newStatus);
    }
  }

  @Override
  public boolean equals(Object o) {

    if (o instanceof BusinessProposal) {
      return  ((BusinessProposal) o).getId().equals(this.id);
    }

    if (o instanceof Negotiation) {
      return  ((Negotiation) o).getId().equals(this.id);
    }

    return Boolean.FALSE;
  }


}
