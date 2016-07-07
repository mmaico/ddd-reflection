package com.trex.proxy;

import com.google.common.collect.Lists;
import com.trex.clone.objects.ddd.negotiation.Negotiation;
import com.trex.clone.objects.ddd.negotiation.NegotiationStatus;
import com.trex.clone.objects.ddd.passenger.Travel;
import com.trex.clone.objects.hibernate_entities.*;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;


public class ProxyHandlerTest {

  @Test
  public void shouldGetValuesUsingProxyWrapper() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);

    assertThat(negotiationProxy.getId(), Matchers.is(1l));
    assertThat(negotiationProxy.getIntroduction(), Matchers.is("test introduction"));
    assertThat(negotiationProxy.getCareOf(), Matchers.is("Smith"));
  }

  @Test
  public void shouldGetNoPrimitiveValuesUsingProxyWrapper() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);

    assertThat(negotiationProxy.getSeller().getId(), Matchers.is(2l));
    assertThat(negotiationProxy.getSeller().getName(), Matchers.is("Jon Snow"));
  }

  @Test
  public void shouldGetAttributeWithDiferentNameHibernateEntity() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);

    assertThat(negotiationProxy.getCustomer().getId(), Matchers.is(3l));
    assertThat(negotiationProxy.getCustomer().getName(), Matchers.is("Tyrion Lannister"));
  }

  @Test
  public void shouldGetUsingFieldConverter() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);

    assertThat(negotiationProxy.getStatus(), Matchers.is(NegotiationStatus.COLD));
  }

  @Test
  public void shouldGetUsingCustomExtractor() {
    final String passportExpected = "PASSPORT585Z44";
    BusinessProposal businessProposal = getHibernateObjectOnRepositoryTwo();

    Travel travel = BusinessModelProxy.from(businessProposal).proxy(Travel.class);

    String passport = travel.getPassenger().getPassport();

    assertThat(passport, Matchers.is(passportExpected));
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
}
