package com.trex.proxy;

import com.google.common.collect.Lists;
import com.trex.test_objects.hibernate_entities.*;
import com.trex.test_objects.model.negotiation.Negotiation;
import com.trex.test_objects.model.negotiation.NegotiationItem;
import com.trex.test_objects.model.negotiation.NegotiationStatus;
import com.trex.test_objects.model.passenger.Aircraft;
import com.trex.test_objects.model.seller.Seller;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;


public class ProxyInterceptorSETTest {

  @Test
  public void shouldSetAttibuteByProxyUsingConvertValueFromModelToHibernateEntity() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);
    negotiationProxy.setStatus(NegotiationStatus.CLOSED_WON);

    assertThat(businessProposal.getTemperature(), Matchers.is(ProposalTemperature.WON));
  }

  @Test
  public void shouldUpdateSellerOnBusinessProposalBySellerNegotiationUsingProxy() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);
    Seller newSeller = new Seller();
    newSeller.setId(2l);
    newSeller.setName("Mr. Anderson");

    negotiationProxy.setSeller(newSeller);

    User seller = businessProposal.getSeller();

    assertThat(seller.getId(), Matchers.is(2l));
    assertThat(seller.getName(), Matchers.is("Mr. Anderson"));
  }

  @Test
  public void shouldUpdateFieldOnBusinessProposalByNegotiationUsingProxy() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);
    negotiationProxy.setCareOf("Test set on proxy");
    negotiationProxy.setIntroduction("Other introduction using proxy");
    negotiationProxy.setId(10l);



    assertThat(businessProposal.getCareOf(), Matchers.is("Test set on proxy"));
    assertThat(businessProposal.getIntroduction(), Matchers.is("Other introduction using proxy"));
    assertThat(businessProposal.getId(), Matchers.is(10l));
  }


  private BusinessProposal getHibernateObjectOnRepository() {
    BusinessProposal businessProposal = new BusinessProposal();
    businessProposal.setId(1l);
    businessProposal.setCareOf("Smith");
    businessProposal.setIntroduction("test introduction");
    businessProposal.setTemperature(ProposalTemperature.INITIAL);

    User user = new User();
    user.setId(2l);
    user.setName("Jon Snow");

    Person person = new Person();
    person.setName("Tyrion Lannister");
    person.setId(3l);

    businessProposal.setSeller(user);
    businessProposal.setClient(person);

    return businessProposal;
  }

  private BusinessProposal getHibernateObjectOnRepositoryTwo() {
    BusinessProposal businessProposal = new BusinessProposal();
    businessProposal.setId(1l);

    User user = new User();
    user.setId(2l);
    user.setName("Jon Snow");

    Document passport = new Document("PASSPORT585Z44", Document.DocumentTypeEnum.PASSPORT);
    Document socialSecurity = new Document("SOCIAL_SECURITY788454X55", Document.DocumentTypeEnum.SOCIAL_SECURITY_CARD);

    user.setDocuments(Lists.newArrayList(passport, socialSecurity));

    businessProposal.setSeller(user);

    return businessProposal;
  }

  private BusinessProposal getHibernateObjectOnRepositoryThree() {
    BusinessProposal businessProposal = new BusinessProposal();
    businessProposal.setId(1l);

    ProposalSaleableItem itemOne = new ProposalSaleableItem();
    itemOne.setId(1l);
    itemOne.setPrice(BigDecimal.TEN);

    ProposalSaleableItem itemTwo = new ProposalSaleableItem();
    itemTwo.setId(2l);
    itemTwo.setPrice(BigDecimal.ONE);

    businessProposal.setSaleableItems(Lists.newArrayList(itemOne, itemTwo));

    return businessProposal;
  }
}
