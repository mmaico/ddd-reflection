package com.github.mmaico.dsl;


import com.google.common.collect.Sets;
import com.github.mmaico.test_objects.hibernate_entities.User;
import com.github.mmaico.test_objects.model.seller.Seller;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

public class DslUsingTest {



    @Test
    public void shouldSetAttributeUsingDSL() {
        Seller seller = new Seller();
        seller.setId(2l);
        seller.setName("seller name");

        User userEntity = new User();


        ConditionalSet.set(userEntity).when(Boolean.TRUE).setName("Marcelo Maico");
        ConditionalSet.set(userEntity).when(Boolean.FALSE).setName("Marcelo Maico 2");


        assertThat(userEntity.getName(), Matchers.is("Marcelo Maico"));

    }

    @Test
    public void shouldSetWhenTrue() {
        Seller seller = new Seller();
        seller.setId(2l);
        seller.setName("seller name");

        User userEntity = new User();


        ConditionalSet.set(userEntity).when(Boolean.TRUE).setName("Marcelo Maico");
        ConditionalSet.set(userEntity).when(Boolean.TRUE).setName("Marcelo Maico 2");


        assertThat(userEntity.getName(), Matchers.is("Marcelo Maico 2"));

    }

    @Test
    public void shouldSetOnlyHasInFields() {
        Seller seller = new Seller();
        seller.setId(2l);
        seller.setName("seller name");
        Set<String> fieldsToUpdate = Sets.newHashSet("name");

        User userEntity = new User();

        ConditionalSet.set(userEntity).ifPresent(fieldsToUpdate).setName("Marcelo Maico");
        ConditionalSet.set(userEntity).ifPresent(fieldsToUpdate).setId(10l);

        assertThat(userEntity.getId(), Matchers.nullValue());
        assertThat(userEntity.getName(), Matchers.is("Marcelo Maico"));
    }
}
