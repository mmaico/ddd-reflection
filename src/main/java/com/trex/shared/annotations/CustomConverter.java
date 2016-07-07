package com.trex.shared.annotations;

import com.trex.shared.converters.AttributeEntityConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CustomConverter {

  Class<? extends AttributeEntityConverter> convert();

  String fieldName() default StringUtils.EMPTY;
}
