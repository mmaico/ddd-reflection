package com.github.mmaico.proxy;


import com.github.mmaico.test_objects.hibernate_entities.*;
import com.github.mmaico.test_objects.model.negotiation.Negotiation;
import com.github.mmaico.test_objects.model.negotiation.NegotiationItem;
import com.github.mmaico.test_objects.model.negotiation.NegotiationStatus;
import com.github.mmaico.test_objects.model.passenger.Aircraft;
import com.google.common.collect.Lists;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Factory;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;


public class ProxyInterceptorGETTest {

  @Test
  public void shouldGetValuesUsingProxyWrapper() {
    BusinessProposal businessProposal = getHibernateObjectOnRepository();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);
    List<Negotiation> list = new ArrayList<>();


    if (negotiationProxy instanceof Factory) {
      Callback callback = ((Factory) negotiationProxy).getCallback(0);
      Object objectModel = ((ProxyInterceptor) callback).getObjectModel();
      Object hibernateEntity = ((ProxyInterceptor) callback).getHibernateEntity();
      System.out.println("testet");
      System.out.println("testet");
      System.out.println("testet");
      System.out.println("testet");
    }


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

    Aircraft aircraft = BusinessModelProxy.from(businessProposal).proxy(Aircraft.class);

    String passport = aircraft.getPassenger().getPassport();

    assertThat(passport, Matchers.is(passportExpected));
  }

  @Test
  public void shouldGetListUsingProxy() {
    BusinessProposal businessProposal = getHibernateObjectOnRepositoryThree();

    Negotiation negotiation = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);

    List<NegotiationItem> items = negotiation.getItems();

    assertThat(items.get(0).getId(), Matchers.is(1l));
    assertThat(items.get(0).getPrice(), Matchers.is(BigDecimal.TEN));

    assertThat(items.get(1).getId(), Matchers.is(2l));
    assertThat(items.get(1).getPrice(), Matchers.is(BigDecimal.ONE));
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
