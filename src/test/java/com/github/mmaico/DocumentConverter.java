package com.github.mmaico;


import com.github.mmaico.shared.converters.AttributeEntityConverter;
import com.github.mmaico.test_objects.hibernate_entities.Document;
import com.github.mmaico.test_objects.model.passenger.Passenger;
import com.github.mmaico.test_objects.hibernate_entities.User;

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
