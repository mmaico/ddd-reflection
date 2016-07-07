package com.trex.proxy;

import com.trex.clone.objects.ddd.negotiation.Negotiation;
import com.trex.clone.objects.ddd.negotiation.NegotiationStatus;
import com.trex.clone.objects.hibernate_entities.BusinessProposal;
import com.trex.clone.objects.hibernate_entities.Person;
import com.trex.clone.objects.hibernate_entities.ProposalTemperature;
import com.trex.clone.objects.hibernate_entities.User;
import net.sf.cglib.proxy.Enhancer;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;


public class ProxyHandlerTest {

  @Test
  public void shouldGetValuesUsingProxyWrapper() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    ProxyHandler proxyHandler = ProxyHandler.create(businessProposal);
    Negotiation negotiationProxy = (Negotiation) Enhancer.create(Negotiation.class, proxyHandler);

    assertThat(negotiationProxy.getId(), Matchers.is(1l));
    assertThat(negotiationProxy.getIntroduction(), Matchers.is("test introduction"));
    assertThat(negotiationProxy.getCareOf(), Matchers.is("Smith"));
  }

  @Test
  public void shouldGetNoPrimitiveValuesUsingProxyWrapper() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    ProxyHandler proxyHandler = ProxyHandler.create(businessProposal);
    Negotiation negotiationProxy = (Negotiation) Enhancer.create(Negotiation.class, proxyHandler);

    assertThat(negotiationProxy.getSeller().getId(), Matchers.is(2l));
    assertThat(negotiationProxy.getSeller().getName(), Matchers.is("Jon Snow"));
  }

  @Test
  public void shouldGetAttributeWithDiferentNameHibernateEntity() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    ProxyHandler proxyHandler = ProxyHandler.create(businessProposal);
    Negotiation negotiationProxy = (Negotiation) Enhancer.create(Negotiation.class, proxyHandler);

    assertThat(negotiationProxy.getCustomer().getId(), Matchers.is(3l));
    assertThat(negotiationProxy.getCustomer().getName(), Matchers.is("Tyrion Lannister"));
  }

  @Test
  public void shouldGetUsingFieldConverter() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    ProxyHandler proxyHandler = ProxyHandler.create(businessProposal);
    Negotiation negotiationProxy = (Negotiation) Enhancer.create(Negotiation.class, proxyHandler);

    assertThat(negotiationProxy.getStatus(), Matchers.is(NegotiationStatus.COLD));
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
}
