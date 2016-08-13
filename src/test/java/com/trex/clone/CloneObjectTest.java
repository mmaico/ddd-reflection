package com.trex.clone;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trex.test_objects.hibernate_entities.BusinessProposal;
import com.trex.test_objects.hibernate_entities.ProposalSaleableItem;
import com.trex.test_objects.hibernate_entities.ProposalTemperature;
import com.trex.test_objects.model.customer.Customer;
import com.trex.test_objects.model.negotiation.AdditionalInformation;
import com.trex.test_objects.model.negotiation.Negotiation;
import com.trex.test_objects.model.negotiation.NegotiationItem;
import com.trex.test_objects.model.negotiation.NegotiationStatus;
import com.trex.test_objects.model.seller.Seller;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

    assertThat(businessProposal.getId(), is(1l));
    assertThat(businessProposal.getClient().getId(), is(2l));
    assertThat(businessProposal.getSeller().getId(), is(3l));
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

    assertThat(businessProposal.getSaleableItems().size(), is(1));
    assertThat(businessProposal.getSaleableItems().get(0).getId(), is(10l));
    assertThat(businessProposal.getSaleableItems().get(0).getPrice(), is(BigDecimal.TEN));
  }

  @Test
  public void shouldConvertAttributeUsingCustomConverter() {
    Negotiation negotiation = new Negotiation();
    negotiation.setStatus(NegotiationStatus.CLOSED_WON);

    BusinessProposal businessProposal = BusinessModelClone.from(negotiation).convertTo(BusinessProposal.class);

    assertThat(businessProposal.getTemperature(), is(ProposalTemperature.WON));
  }

  @Test
  public void shouldMergeObjects() {
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

    BusinessProposal businessProposal = new BusinessProposal();
    BusinessModelClone.from(negotiation).merge(businessProposal);

    assertThat(businessProposal.getId(), is(1l));
    assertThat(businessProposal.getClient().getId(), is(2l));
    assertThat(businessProposal.getSeller().getId(), is(3l));
  }

  @Test
  public void shouldMergeObjectsWithoutStackoverFlowOnCircularReference () {
    Negotiation negotiation = getObjectWithCircularReference();

    BusinessProposal businessProposal = new BusinessProposal();

    BusinessModelClone.from(negotiation).merge(businessProposal);
    ProposalSaleableItem proposalSaleableItem = businessProposal.getSaleableItems().get(0);

    assertThat(businessProposal.getId(), is(22l));
    assertThat(proposalSaleableItem.getId(), is(1l));
    assertThat(proposalSaleableItem.getQuantity(), is(2));
    assertThat(businessProposal.getSaleableItems(), hasSize(1));

    assertThat(proposalSaleableItem.getBusinessProposal().getId(), is(22l));

    assertThat(businessProposal.getInformation().getId(), is(33l));
    assertThat(businessProposal.getInformation().getDescription(), is("info"));

    assertThat(businessProposal.getInformation().getBusinessProposal().getId(), is(22l));
  }

  @Test
  public void shouldCopyOnlyFieldsInformed() {
    Negotiation negotiation = new Negotiation();
    negotiation.setId(22l);
    negotiation.setCareOf("Marcelo Maico");
    negotiation.setIntroduction("Introduction here");

    negotiation.setUpdateAttributes(Sets.newHashSet("careOf", "introduction"));

    BusinessProposal businessProposal = new BusinessProposal();

    BusinessModelClone.from(negotiation).merge(businessProposal);

    MatcherAssert.assertThat(businessProposal.getCareOf(), is("Marcelo Maico"));
    MatcherAssert.assertThat(businessProposal.getIntroduction(), is("Introduction here"));
    MatcherAssert.assertThat(businessProposal.getId(), nullValue());
  }

  @Test
  public void shouldNoCopyFieldOfNestedObjectWhenNoPresentInUpdateAttributes() {
    Negotiation negotiation = new Negotiation();
    negotiation.setId(22l);
    negotiation.setCareOf("Marcelo Maico");
    negotiation.setIntroduction("Introduction here");

    AdditionalInformation information = new AdditionalInformation();
    information.setId(10l);

    negotiation.setInformation(information);

    negotiation.setUpdateAttributes(Sets.newHashSet("careOf"));

    BusinessProposal businessProposal = new BusinessProposal();

    BusinessModelClone.from(negotiation).merge(businessProposal);

    MatcherAssert.assertThat(businessProposal.getCareOf(), is("Marcelo Maico"));
    MatcherAssert.assertThat(businessProposal.getIntroduction(), nullValue());
    MatcherAssert.assertThat(businessProposal.getInformation(), nullValue());
    MatcherAssert.assertThat(businessProposal.getId(), nullValue());
  }

  private Negotiation getObjectWithCircularReference() {
    Negotiation negotiation = new Negotiation();
    negotiation.setId(22l);

    AdditionalInformation information = new AdditionalInformation();
    information.setNegotiation(negotiation);
    information.setId(33l);
    information.setDescription("info");

    NegotiationItem item = new NegotiationItem();
    item.setId(1l);
    item.setQuantity(2);
    item.setPrice(BigDecimal.ONE);
    item.setNegotiation(negotiation);

    negotiation.setItems(Lists.newArrayList(item));
    negotiation.setInformation(information);

    return negotiation;
  }

}
