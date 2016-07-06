package com.trex.clone.objects.hibernate_entities;

import java.util.Date;
import java.util.List;

//@Entity
//@Table(name = "business_proposal")
public class BusinessProposal {


  /**
   *
   */
  private static final long serialVersionUID = -3466031805155434986L;

  //@Id
  //@GeneratedValue
  private Long id;

//  @ManyToOne
//  @JoinColumn(name = "client_id")
  private Person client;

//  @ManyToOne
//  @JoinColumn(name = "seller_id")
  private User seller;

  private String careOf;

//  @Temporal(TemporalType.TIMESTAMP)
//  @DateTimeFormat(pattern = "dd/M/Y")
  private Date deliveryForeCast;

  private String introduction;

  //@OneToMany(cascade = CascadeType.ALL, mappedBy = "businessProposal")
  private List<ProposalSaleableItem> saleableItems;

  public BusinessProposal() {
  }

  public BusinessProposal(Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public Person getClient() {
    return client;
  }

  public void setClient(Person client) {
    this.client = client;
  }

  public User getSeller() {
    return seller;
  }

  public void setSeller(User seller) {
    this.seller = seller;
  }

  public String getCareOf() {
    return careOf;
  }

  public void setCareOf(String careOf) {
    this.careOf = careOf;
  }

  public Date getDeliveryForeCast() {
    return deliveryForeCast;
  }

  public void setDeliveryForeCast(Date deliveryForeCast) {
    this.deliveryForeCast = deliveryForeCast;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public List<ProposalSaleableItem> getSaleableItems() {
    return saleableItems;
  }

  public void setSaleableItems(List<ProposalSaleableItem> saleableItems) {
    this.saleableItems = saleableItems;
  }

}
