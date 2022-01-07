
package com.github.mmaico.proxy.extractors;


public interface AttributeExtractor<ENTITY, RETURN_ENTITY,MODEL, RETURN_MODEL> {

	RETURN_ENTITY getAttributeValueEntity(ENTITY attribute);

	RETURN_MODEL getAttributeValueModel(MODEL dbData);

}
