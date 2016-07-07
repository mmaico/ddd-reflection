package com.trex;


import com.trex.test_objects.model.negotiation.NegotiationStatus;
import com.trex.test_objects.hibernate_entities.ProposalTemperature;
import com.trex.shared.converters.AttributeEntityConverter;

public class NegotiationStatusEnumConverter implements AttributeEntityConverter<NegotiationStatus, ProposalTemperature> {

  @Override public ProposalTemperature convertToEntityAttribute(NegotiationStatus attribute) {
    if (attribute == null) {
      return null;
    } else if (attribute == NegotiationStatus.COLD) {
      return ProposalTemperature.INITIAL;
    } else if(attribute == NegotiationStatus.CLOSED_WON) {
      return ProposalTemperature.WON;
    } else if(attribute == NegotiationStatus.HOT) {
      return ProposalTemperature.ALMOST_CLOSING;
    }

    return null;
  }

  @Override public NegotiationStatus convertToBusinessModel(ProposalTemperature attribute) {
    if (attribute == null) {
      return null;
    } else if (attribute == ProposalTemperature.INITIAL) {
      return NegotiationStatus.COLD;
    } else if(attribute == ProposalTemperature.WON) {
      return NegotiationStatus.CLOSED_WON ;
    } else if(attribute == ProposalTemperature.ALMOST_CLOSING) {
      return NegotiationStatus.HOT;
    }

    return null;
  }
}
