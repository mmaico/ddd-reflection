package com.trex;


import com.trex.test_objects.hibernate_entities.BusinessProposal;
import com.trex.test_objects.model.negotiation.Negotiation;
import com.trex.test_objects.model.negotiation.NegotiationStatus;
import com.trex.test_objects.hibernate_entities.ProposalTemperature;
import com.trex.shared.converters.AttributeEntityConverter;

public class NegotiationStatusEnumConverter implements AttributeEntityConverter<Negotiation, BusinessProposal> {

  @Override public Object convertToEntityAttribute(Negotiation nagotiation) {
    if (nagotiation.getStatus() == null) {
      return null;
    } else if (nagotiation.getStatus() == NegotiationStatus.COLD) {
      return ProposalTemperature.INITIAL;
    } else if(nagotiation.getStatus() == NegotiationStatus.CLOSED_WON) {
      return ProposalTemperature.WON;
    } else if(nagotiation.getStatus() == NegotiationStatus.HOT) {
      return ProposalTemperature.ALMOST_CLOSING;
    }

    return null;
  }

  @Override public Object convertToBusinessModel(BusinessProposal attribute) {
    if (attribute.getTemperature() == null) {
      return null;
    } else if (attribute.getTemperature() == ProposalTemperature.INITIAL) {
      return NegotiationStatus.COLD;
    } else if(attribute.getTemperature() == ProposalTemperature.WON) {
      return NegotiationStatus.CLOSED_WON ;
    } else if(attribute.getTemperature() == ProposalTemperature.ALMOST_CLOSING) {
      return NegotiationStatus.HOT;
    } else if(attribute.getTemperature() == ProposalTemperature.LOST) {
      return NegotiationStatus.CLOSED_LOST;
    }

    return null;
  }
}
