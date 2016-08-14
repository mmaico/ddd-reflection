package com.trex.test_objects.model.customer;



import com.trex.shared.annotations.EntityReference;
import com.trex.test_objects.hibernate_entities.Person;
import com.trex.test_objects.model.negotiation.AbstractModel;

@EntityReference(Person.class)
public class Customer  extends AbstractModel {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
