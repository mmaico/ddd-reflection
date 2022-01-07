
package com.github.mmaico.shared.converters;


public interface AttributeEntityConverter<MODEL, ENTITY> {

	Object convertToEntityAttribute(MODEL entity);

	Object convertToBusinessModel(ENTITY dbData);

}
