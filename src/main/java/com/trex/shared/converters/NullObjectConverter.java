package com.trex.shared.converters;


public class NullObjectConverter implements AttributeEntityConverter<Object, Object> {

  @Override public Object convertToEntityAttribute(Object attribute) {
    return null;
  }

  @Override public Object convertToBusinessModel(Object dbData) {
    return null;
  }

}
