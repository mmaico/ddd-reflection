package com.trex;


import com.trex.clone.objects.ddd.passenger.Passenger;
import com.trex.clone.objects.hibernate_entities.Document;
import com.trex.clone.objects.hibernate_entities.User;
import com.trex.proxy.extractors.AttributeExtractor;

import java.util.Optional;

public class DocumentExtractor implements AttributeExtractor<User,String, Passenger, String> {

  @Override public String getAttributeValueEntity(User user) {
    Optional<Document> documentFound = user.getDocuments().stream()
        .filter(item -> item.getType() == Document.DocumentTypeEnum.PASSPORT).findFirst();

    return documentFound.isPresent() ? documentFound.get().getDocument() : null;
  }

  @Override public String getAttributeValueModel(Passenger attribute) {

    return null;
  }
}
