package com.trex.clone.annotations;




import com.trex.clone.converters.AttributeConverter;
import com.trex.clone.converters.NullObjectConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface EntityReference {

  Class value();

  String fieldName() default StringUtils.EMPTY;

  Class<? extends AttributeConverter> convert() default NullObjectConverter.class;
}
