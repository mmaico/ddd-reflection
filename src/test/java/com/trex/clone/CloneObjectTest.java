package com.trex.clone;


import com.google.common.collect.Lists;
import com.trex.test_objects.hibernate_entities.BusinessProposal;
import com.trex.test_objects.hibernate_entities.ProposalTemperature;
import com.trex.test_objects.model.customer.Customer;
import com.trex.test_objects.model.negotiation.Negotiation;
import com.trex.test_objects.model.negotiation.NegotiationItem;
import com.trex.test_objects.model.negotiation.NegotiationStatus;
import com.trex.test_objects.model.seller.Seller;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

public class CloneObjectTest {

  @Test
  public void shouldCloneObject() {

    Negotiation negotiation = new Negotiation();
    negotiation.setId(1l);
    negotiation.setCareOf("EU");
    negotiation.setIntroduction("Introduction");
    negotiation.setStatus(NegotiationStatus.CLOSED_WON);

    Customer customer = new Customer();
    customer.setId(2l);

    Seller seller = new Seller();
    seller.setId(3l);

    negotiation.setCustomer(customer);
    negotiation.setSeller(seller);

    NegotiationItem itemOne = new NegotiationItem();
    itemOne.setId(10l);
    itemOne.setPrice(BigDecimal.TEN);

    negotiation.setItems(Lists.newArrayList(itemOne));

    BusinessProposal businessProposal = BusinessModelClone.from(negotiation).convertTo(BusinessProposal.class);

    assertThat(businessProposal.getId(), Matchers.is(1l));
    assertThat(businessProposal.getClient().getId(), Matchers.is(2l));
    assertThat(businessProposal.getSeller().getId(), Matchers.is(3l));
  }

  @Test
  public void shouldCloneObjectWithList() {
    Negotiation negotiation = new Negotiation();
    negotiation.setId(1l);
    negotiation.setCareOf("EU");
    negotiation.setIntroduction("Introduction");
    negotiation.setStatus(NegotiationStatus.CLOSED_WON);

    NegotiationItem itemOne = new NegotiationItem();
    itemOne.setId(10l);
    itemOne.setPrice(BigDecimal.TEN);

    negotiation.setItems(Lists.newArrayList(itemOne));

    BusinessProposal businessProposal = BusinessModelClone.from(negotiation).convertTo(BusinessProposal.class);

    assertThat(businessProposal.getSaleableItems().size(), Matchers.is(1));
    assertThat(businessProposal.getSaleableItems().get(0).getId(), Matchers.is(10l));
    assertThat(businessProposal.getSaleableItems().get(0).getPrice(), Matchers.is(BigDecimal.TEN));
  }

  @Test
  public void shouldConvertAttributeUsingCustomConverter() {
    Negotiation negotiation = new Negotiation();
    negotiation.setStatus(NegotiationStatus.CLOSED_WON);

    BusinessProposal businessProposal = BusinessModelClone.from(negotiation).convertTo(BusinessProposal.class);

    assertThat(businessProposal.getTemperature(), Matchers.is(ProposalTemperature.WON));
  }
}
