package com.github.mmaico.test_objects.model.negotiation;


import com.github.mmaico.shared.annotations.Attribute;
import com.github.mmaico.shared.annotations.Model;
import com.github.mmaico.test_objects.hibernate_entities.ProposalSaleableItem;

import java.math.BigDecimal;

@Model
public class NegotiationItem extends AbstractModel {

  private Long id;

  private Product product;

  private Integer quantity;

  private BigDecimal price;

  @Attribute(destinationName = "businessProposal")
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
