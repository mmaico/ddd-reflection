
package com.trex.clone.converters;


public interface AttributeConverter<X,Y> {

	Y convertToEntityAttribute(X attribute);


	X convertToBusinessModel(Y dbData);
}
