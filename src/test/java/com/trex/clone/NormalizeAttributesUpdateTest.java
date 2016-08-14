package com.trex.clone;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trex.test_objects.model.negotiation.AdditionalInformation;
import com.trex.test_objects.model.negotiation.Negotiation;
import com.trex.test_objects.model.negotiation.NegotiationItem;
import com.trex.test_objects.model.negotiation.Product;
import com.trex.test_objects.model.seller.Seller;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;


public class NormalizeAttributesUpdateTest {


    @Test
    public void shouldAddAttributesToUpdateInEntities() {
        Set<String> attrToUpdate = Sets.newHashSet();
        attrToUpdate.add("introduction");
        attrToUpdate.add("careOf");
        attrToUpdate.add("seller.name");
        attrToUpdate.add("seller.id");
        attrToUpdate.add("information.description");

        Negotiation negotiation = new Negotiation();
        negotiation.setInformation(new AdditionalInformation());
        negotiation.setSeller(new Seller());

        new NormalizeAttributesUpdate().addFieldsToUpdate(negotiation, attrToUpdate);

        assertThat(negotiation.getUpdateAttributes().contains("introduction"), Matchers.is(Boolean.TRUE));
        assertThat(negotiation.getUpdateAttributes().contains("careOf"), Matchers.is(Boolean.TRUE));
        assertThat(negotiation.getUpdateAttributes().contains("seller"), Matchers.is(Boolean.TRUE));
        assertThat(negotiation.getSeller().getUpdateAttributes().contains("name"), Matchers.is(Boolean.TRUE));
        assertThat(negotiation.getSeller().getUpdateAttributes().contains("id"), Matchers.is(Boolean.TRUE));
        assertThat(negotiation.getInformation().getUpdateAttributes().contains("description"), Matchers.is(Boolean.TRUE));
    }

    @Test
    public void shouldAddAttributeToUpdateInList() {
        Set<String> attrToUpdate = Sets.newHashSet();
        attrToUpdate.add("items[0].id");
        attrToUpdate.add("items[0].quantity");
        attrToUpdate.add("items[0].price");
        attrToUpdate.add("items[0].product");
        attrToUpdate.add("items[0].product.id");


        Negotiation negotiation = new Negotiation();
        NegotiationItem item = new NegotiationItem();
        Product product = new Product();
        item.setProduct(product);

        negotiation.setItems(Lists.newArrayList(item));


        new NormalizeAttributesUpdate().addFieldsToUpdate(negotiation, attrToUpdate);

        assertThat(negotiation.getItems().get(0).getUpdateAttributes().contains("id"), Matchers.is(Boolean.TRUE));
        assertThat(negotiation.getItems().get(0).getUpdateAttributes().contains("quantity"), Matchers.is(Boolean.TRUE));
        assertThat(negotiation.getItems().get(0).getUpdateAttributes().contains("price"), Matchers.is(Boolean.TRUE));
        assertThat(negotiation.getItems().get(0).getUpdateAttributes().contains("product"), Matchers.is(Boolean.TRUE));
        assertThat(negotiation.getItems().get(0).getProduct().getUpdateAttributes().contains("id"), Matchers.is(Boolean.TRUE));

    }

}