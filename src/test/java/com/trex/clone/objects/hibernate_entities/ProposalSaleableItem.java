package com.trex.clone.objects.hibernate_entities;


import java.math.BigDecimal;

//@Entity
//@Table(name="proposal_saleable_item")
public class ProposalSaleableItem {

  /**
   *
   */
  private static final long serialVersionUID = -3881704814612452364L;

//  @Id
//  @GeneratedValue
  private Long id;

  private BigDecimal price;

  private BigDecimal originalPrice;

  private Integer quantity = 0;

//  @ManyToOne
//  @JoinColumn(name = "business_proposal_id")
//  @ExcludeAuditingField
  private BusinessProposal businessProposal;


  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal getOriginalPrice() {
    return originalPrice;
  }

  public void setOriginalPrice(BigDecimal originalPrice) {
    this.originalPrice = originalPrice;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BusinessProposal getBusinessProposal() {
    return businessProposal;
  }

  public void setBusinessProposal(BusinessProposal businessProposal) {
    this.businessProposal = businessProposal;
  }

}
