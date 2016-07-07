package com.trex.clone.objects.ddd.customer;



import com.trex.shared.annotations.EntityReference;
import com.trex.clone.objects.hibernate_entities.Person;

@EntityReference(Person.class)
public class Customer {

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
