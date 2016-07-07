package com.trex.clone.objects.ddd.negotiation;



import com.trex.shared.annotations.EntityReference;
import com.trex.clone.objects.ddd.customer.Customer;
import com.trex.clone.objects.ddd.seller.Seller;
import com.trex.clone.objects.hibernate_entities.BusinessProposal;
import com.trex.clone.objects.hibernate_entities.Person;
import com.trex.clone.objects.hibernate_entities.ProposalSaleableItem;
import com.trex.clone.objects.hibernate_entities.User;

import java.util.List;

@EntityReference(BusinessProposal.class)
public class Negotiation {

  private Long id;

  private String introduction;

  private String careOf;

  @EntityReference(User.class)
  private Seller seller;

  @EntityReference(value = Person.class, fieldName = "client")
  private Customer customer;

  @EntityReference(value = ProposalSaleableItem.class, fieldName = "saleableItems")
  private List<NegotiationItem> items;


  private NegotiationStatus status;


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

  public void changeStatusTo(NegotiationStatus newStatus) {
    if (!this.getStatus().equals(newStatus)) {
        this.setStatus(newStatus);
    }
  }
}
