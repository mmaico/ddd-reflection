package com.trex.shared.annotations;



import com.trex.proxy.extractors.AttributeExtractor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Extractor {

  Class<? extends AttributeExtractor> value();

}
