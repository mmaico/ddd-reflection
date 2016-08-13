package com.trex.test_objects.model.negotiation;


import com.trex.shared.annotations.EntityReference;
import com.trex.test_objects.hibernate_entities.BusinessProposal;
import com.trex.test_objects.hibernate_entities.ProductLegacy;
import com.trex.test_objects.hibernate_entities.ProposalSaleableItem;

import java.math.BigDecimal;

@EntityReference(ProposalSaleableItem.class)
public class NegotiationItem {

  private Long id;

  @EntityReference(value = ProductLegacy.class)
  private Product product;

  private Integer quantity;

  private BigDecimal price;

  @EntityReference(value = BusinessProposal.class, fieldName = "businessProposal")
  private Negotiation negotiation;

  public NegotiationItem () {}
  public NegotiationItem (Long id, Product product, Integer quantity, BigDecimal price) {
    this.id = id;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Negotiation getNegotiation() {
    return negotiation;
  }

  public void setNegotiation(Negotiation negotiation) {
    this.negotiation = negotiation;
  }

  @Override
  public boolean equals(Object o) {

    if (o instanceof ProposalSaleableItem) {
      return  ((ProposalSaleableItem) o).getId().equals(this.id);
    }

    if (o instanceof NegotiationItem) {
      return  ((Negotiation) o).getId().equals(this.id);
    }

    return Boolean.FALSE;
  }
}
