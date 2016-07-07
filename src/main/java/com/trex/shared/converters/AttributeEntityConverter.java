
package com.trex.shared.converters;


public interface AttributeEntityConverter<X,Y> {

	Y convertToEntityAttribute(X attribute);

	X convertToBusinessModel(Y dbData);

}
