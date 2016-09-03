package com.trex;


import com.trex.shared.converters.AttributeEntityConverter;
import com.trex.test_objects.hibernate_entities.BusinessProposal;
import com.trex.test_objects.model.negotiation.Negotiation;
import com.trex.test_objects.model.passenger.Passenger;
import com.trex.test_objects.hibernate_entities.Document;
import com.trex.test_objects.hibernate_entities.User;
import com.trex.proxy.extractors.AttributeExtractor;

import java.util.Optional;

public class DocumentConverter implements AttributeEntityConverter<Passenger, User> {


  @Override
  public Object convertToEntityAttribute(Passenger passenger) {
    return null;
  }

  @Override
  public Object convertToBusinessModel(User user) {
    Optional<Document> documentFound = user.getDocuments().stream()
            .filter(item -> item.getType() == Document.DocumentTypeEnum.PASSPORT).findFirst();

    return documentFound.isPresent() ? documentFound.get().getDocument() : null;
  }
}
