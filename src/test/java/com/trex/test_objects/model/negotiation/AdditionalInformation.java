package com.trex.test_objects.model.negotiation;


import com.trex.shared.annotations.EntityReference;
import com.trex.test_objects.hibernate_entities.Information;

@EntityReference(Information.class)
public class AdditionalInformation {

    private Long id;

    @EntityReference(value = Negotiation.class, fieldName = "businessProposal")
    private Negotiation negotiation;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Negotiation getNegotiation() {
        return negotiation;
    }

    public void setNegotiation(Negotiation negotiation) {
        this.negotiation = negotiation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
