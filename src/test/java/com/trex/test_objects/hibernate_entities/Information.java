package com.trex.test_objects.hibernate_entities;

public class Information {

    private Long id;
    private BusinessProposal businessProposal;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusinessProposal getBusinessProposal() {
        return businessProposal;
    }

    public void setBusinessProposal(BusinessProposal businessProposal) {
        this.businessProposal = businessProposal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
