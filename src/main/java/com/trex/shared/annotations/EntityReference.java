package com.trex.shared.annotations;




import com.trex.shared.converters.AttributeEntityConverter;
import com.trex.shared.converters.NullObjectConverter;
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

  Class<? extends AttributeEntityConverter> convert() default NullObjectConverter.class;
}
