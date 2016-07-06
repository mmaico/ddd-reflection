package com.trex.clone.converters;


public class NullObjectConverter implements AttributeConverter<Object, Object> {

  @Override public Object convertToEntityAttribute(Object attribute) {
    return null;
  }

  @Override public Object convertToBusinessModel(Object dbData) {
    return null;
  }
}
