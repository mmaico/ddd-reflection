package com.trex.proxy;

import com.google.common.collect.Lists;
import com.trex.test_objects.hibernate_entities.*;
import com.trex.test_objects.model.negotiation.Negotiation;
import com.trex.test_objects.model.negotiation.NegotiationItem;
import com.trex.test_objects.model.negotiation.NegotiationStatus;
import com.trex.test_objects.model.negotiation.Product;
import com.trex.test_objects.model.passenger.Aircraft;
import com.trex.test_objects.model.seller.Seller;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
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

  @Test
  public void shouldConvertAndSetCollectionOnProxyInBusinessProposal() {
    BusinessProposal businessProposal = getHibernateObjectOnRepositoryThree();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);
    negotiationProxy.setItems(getNegotiationItems());

    List<ProposalSaleableItem> saleableItems = businessProposal.getSaleableItems();


    assertThat(saleableItems.get(0).getId(), Matchers.is(1l));
    assertThat(saleableItems.get(0).getPrice(), Matchers.is(BigDecimal.TEN));
    assertThat(saleableItems.get(0).getQuantity(), Matchers.is(2));
    assertThat(saleableItems.get(0).getProduct().getId(), Matchers.is(1l));
    assertThat(saleableItems.get(0).getProduct().getName(), Matchers.is("Product one"));

    assertThat(saleableItems.get(1).getId(), Matchers.is(2l));
    assertThat(saleableItems.get(1).getPrice(), Matchers.is(BigDecimal.ONE));
    assertThat(saleableItems.get(1).getQuantity(), Matchers.is(3));
    assertThat(saleableItems.get(1).getProduct().getId(), Matchers.is(2l));
    assertThat(saleableItems.get(1).getProduct().getName(), Matchers.is("Product two"));

  }

  @Test
  public void shouldMergeCollectionOnSetProxyInBusinessProposal() {
    BusinessProposal businessProposal = getHibernateObjectOnRepositoryWithProposalSaleableItems();

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);
    negotiationProxy.setItems(getNegotiationItems());

    List<ProposalSaleableItem> saleableItems = businessProposal.getSaleableItems();


    assertThat(saleableItems.get(0).getId(), Matchers.is(1l));
    assertThat(saleableItems.get(0).getPrice(), Matchers.is(BigDecimal.TEN));
    assertThat(saleableItems.get(0).getQuantity(), Matchers.is(2));
    assertThat(saleableItems.get(0).getProduct().getId(), Matchers.is(1l));
    assertThat(saleableItems.get(0).getProduct().getName(), Matchers.is("Product one"));

    assertThat(saleableItems.get(1).getId(), Matchers.is(2l));
    assertThat(saleableItems.get(1).getPrice(), Matchers.is(BigDecimal.ONE));
    assertThat(saleableItems.get(1).getQuantity(), Matchers.is(3));
    assertThat(saleableItems.get(1).getProduct().getId(), Matchers.is(2l));
    assertThat(saleableItems.get(1).getProduct().getName(), Matchers.is("Product two"));

  }

  @Test
  public void shouldAddCollectionWhenNotExistInHibernateObject() {
    BusinessProposal businessProposal = getHibernateObjectOnRepositoryWithProposalSaleableItems();
    businessProposal.setSaleableItems(null);

    Negotiation negotiationProxy = BusinessModelProxy.from(businessProposal).proxy(Negotiation.class);
    negotiationProxy.setItems(getNegotiationItems());

    List<ProposalSaleableItem> saleableItems = businessProposal.getSaleableItems();


    assertThat(saleableItems.get(0).getId(), Matchers.is(1l));
    assertThat(saleableItems.get(0).getPrice(), Matchers.is(BigDecimal.TEN));
    assertThat(saleableItems.get(0).getQuantity(), Matchers.is(2));
    assertThat(saleableItems.get(0).getProduct().getId(), Matchers.is(1l));
    assertThat(saleableItems.get(0).getProduct().getName(), Matchers.is("Product one"));

    assertThat(saleableItems.get(1).getId(), Matchers.is(2l));
    assertThat(saleableItems.get(1).getPrice(), Matchers.is(BigDecimal.ONE));
    assertThat(saleableItems.get(1).getQuantity(), Matchers.is(3));
    assertThat(saleableItems.get(1).getProduct().getId(), Matchers.is(2l));
    assertThat(saleableItems.get(1).getProduct().getName(), Matchers.is("Product two"));

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

    return businessProposal;
  }

  private BusinessProposal getHibernateObjectOnRepositoryWithProposalSaleableItems() {
    BusinessProposal businessProposal = new BusinessProposal();
    businessProposal.setId(1l);

    ProductLegacy productLegacy = new ProductLegacy();
    productLegacy.setId(1l);
    productLegacy.setName("Product 1");

    ProductLegacy productLegacy2 = new ProductLegacy();
    productLegacy2.setId(2l);
    productLegacy2.setName("Product 2");

    ProposalSaleableItem one = new ProposalSaleableItem();
    one.setId(1l);
    one.setOriginalPrice(BigDecimal.ONE);
    one.setQuantity(30);
    one.setProduct(productLegacy);

    ProposalSaleableItem two = new ProposalSaleableItem();
    two.setId(2l);
    two.setOriginalPrice(BigDecimal.ZERO);
    two.setQuantity(20);
    two.setProduct(productLegacy2);

    businessProposal.setSaleableItems(Lists.newArrayList(one, two));

    return businessProposal;
  }

  public List<NegotiationItem> getNegotiationItems() {
    Product productOne = new Product();
    productOne.setName("Product one");
    productOne.setId(1l);

    Product productTwo = new Product();
    productTwo.setName("Product two");
    productTwo.setId(2l);

    List<NegotiationItem> items = new ArrayList<>();
    items.add(new NegotiationItem(1l, productOne, 2, BigDecimal.TEN));
    items.add(new NegotiationItem(2l, productTwo, 3, BigDecimal.ONE));

    return items;
  }
}
