package com.github.mmaico.test_objects.model.negotiation;


import com.github.mmaico.shared.annotations.Attribute;
import com.github.mmaico.shared.annotations.Model;

@Model
public class AdditionalInformation extends AbstractModel {

    private Long id;

    @Attribute(destinationName = "businessProposal")
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
