package com.github.mmaico.clone;


import com.github.mmaico.test_objects.hibernate_entities.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trex.test_objects.hibernate_entities.*;
import com.github.mmaico.test_objects.model.customer.Customer;
import com.github.mmaico.test_objects.model.negotiation.AdditionalInformation;
import com.github.mmaico.test_objects.model.negotiation.Negotiation;
import com.github.mmaico.test_objects.model.negotiation.NegotiationItem;
import com.github.mmaico.test_objects.model.negotiation.NegotiationStatus;
import com.github.mmaico.test_objects.model.seller.Seller;
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
    customer.setName("Name of customer");
    customer.setId(2l);

    Seller seller = new Seller();
    seller.setName("Jose Luiz");
    seller.setId(3l);

    negotiation.setCustomer(customer);
    negotiation.setSeller(seller);

    NegotiationItem itemOne = new NegotiationItem();
    itemOne.setId(10l);
    itemOne.setPrice(BigDecimal.TEN);

    negotiation.setItems(Lists.newArrayList(itemOne));

    BusinessProposal businessProposal = BusinessModelClone.from(negotiation).convertTo(BusinessProposal.class);

    assertThat(businessProposal.getId(), is(1l));
    assertThat(businessProposal.getSeller().getId(), is(3l));
    assertThat(businessProposal.getSeller().getName(), is("Jose Luiz"));
    assertThat(businessProposal.getClient().getId(), is(2l));
    assertThat(businessProposal.getClient().getName(), is("Name of customer"));

    assertThat(businessProposal.getSaleableItems().size(), is(1));
    assertThat(businessProposal.getSaleableItems().get(0).getId(), is(10l));
    assertThat(businessProposal.getSaleableItems().get(0).getPrice(), is(BigDecimal.TEN));


  }

  @Test
  public void shouldCloneObjectInverted() {

    BusinessProposal proposal = new BusinessProposal();
    proposal.setId(1l);
    proposal.setCareOf("EU");
    proposal.setIntroduction("Introduction");
    proposal.setTemperature(ProposalTemperature.WON);

    Person client = new Person();
    client.setId(4l);
    client.setName("name of person");

    User seller = new User();
    seller.setName("Jose Luiz");
    seller.setId(3l);

    proposal.setClient(client);
    proposal.setSeller(seller);

    ProposalSaleableItem itemOne = new ProposalSaleableItem();
    itemOne.setId(10l);
    itemOne.setPrice(BigDecimal.TEN);

    proposal.setSaleableItems(Lists.newArrayList(itemOne));

    Negotiation negotiation = BusinessModelClone.from(proposal).convertTo(Negotiation.class);

    assertThat(negotiation.getCareOf(), is("EU"));
    assertThat(negotiation.getIntroduction(), is("Introduction"));
    assertThat(negotiation.getId(), is(1l));
    assertThat(negotiation.getSeller().getId(), is(3l));
    assertThat(negotiation.getSeller().getName(), is("Jose Luiz"));
    assertThat(negotiation.getCustomer().getId(), is(4l));
    assertThat(negotiation.getCustomer().getName(), is("name of person"));

    assertThat(negotiation.getStatus(), is(NegotiationStatus.CLOSED_WON));
    assertThat(negotiation.getItems().size(), is(1));
    assertThat(negotiation.getItems().get(0).getId(), is(10l));
    assertThat(negotiation.getItems().get(0).getPrice(), is(BigDecimal.TEN));

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
  public void shouldConvertInverse() {
    BusinessProposal proposal = new BusinessProposal();
    proposal.setId(1l);
    proposal.setCareOf("EU");
    proposal.setIntroduction("Introduction");
    proposal.setTemperature(ProposalTemperature.LOST);

    Person customer = new Person();
    customer.setId(2l);

    User seller = new User();
    seller.setId(3l);

    proposal.setClient(customer);
    proposal.setSeller(seller);

    ProposalSaleableItem itemOne = new ProposalSaleableItem();
    itemOne.setId(10l);
    itemOne.setPrice(BigDecimal.TEN);

    proposal.setSaleableItems(Lists.newArrayList(itemOne));


    Negotiation negotiation = BusinessModelClone.from(proposal).convertTo(Negotiation.class);

    assertThat(negotiation.getId(), is(1l));
    assertThat(negotiation.getCustomer().getId(), is(2l));
    assertThat(negotiation.getSeller().getId(), is(3l));
    assertThat(negotiation.getCareOf(), is("EU"));
    assertThat(negotiation.getIntroduction(), is("Introduction"));
    assertThat(negotiation.getStatus(), is(NegotiationStatus.CLOSED_LOST));
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
