package com.trex.proxy;

import com.trex.clone.objects.ddd.negotiation.Negotiation;
import com.trex.clone.objects.hibernate_entities.BusinessProposal;
import com.trex.clone.objects.hibernate_entities.User;
import net.sf.cglib.proxy.Enhancer;
import org.hamcrest.MatcherAssert;
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

  private BusinessProposal getHibernateObjectOnRepository() {
    BusinessProposal businessProposal = new BusinessProposal();
    businessProposal.setId(1l);
    businessProposal.setCareOf("Smith");
    businessProposal.setIntroduction("test introduction");
    User user = new User();
    user.setId(1l);
    businessProposal.setSeller(user);

    return businessProposal;
  }
}
