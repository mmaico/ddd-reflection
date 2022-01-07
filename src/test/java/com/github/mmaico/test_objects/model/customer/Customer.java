package com.github.mmaico.test_objects.model.customer;


import com.github.mmaico.shared.annotations.Model;
import com.github.mmaico.test_objects.model.negotiation.AbstractModel;

@Model
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
