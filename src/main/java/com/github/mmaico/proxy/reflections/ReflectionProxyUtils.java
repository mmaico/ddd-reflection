package com.github.mmaico.proxy.reflections;


import com.github.mmaico.proxy.extractors.AttributeExtractor;
import com.github.mmaico.shared.annotations.Attribute;
import com.github.mmaico.shared.annotations.CustomConverter;
import com.github.mmaico.shared.annotations.Extractor;
import com.github.mmaico.shared.converters.AttributeEntityConverter;
import com.github.mmaico.shared.libraries.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ReflectionProxyUtils {

  public static String getHibernateEntityFieldNameBy(Object objectModel, String originMethodName) {
    String fieldName = StringUtils.uncapitalize(originMethodName.replaceAll("(get|set)", ""));

    Optional<Field> field = ReflectionUtils.getField(objectModel, fieldName);
    Field fieldFound = field.get();
    if (fieldFound.getAnnotation(Attribute.class) != null) {
      Attribute referenceEntityFound = fieldFound.getAnnotation(Attribute.class);
      return isBlank(referenceEntityFound.destinationName()) ? fieldName : referenceEntityFound.destinationName();
    } else if (fieldFound.getAnnotation(CustomConverter.class) != null) {
      CustomConverter customConverterFound = fieldFound.getAnnotation(CustomConverter.class);
      return isBlank(customConverterFound.fieldName()) ? fieldName : customConverterFound.fieldName();
    }

    return fieldName;
  }

  public static Optional<Field> getFieldByGetOrSet(Object objectModel, String originMethodName) {
    String fieldName = StringUtils.uncapitalize(originMethodName.replaceAll("(get|set)", ""));

    Optional<Field> field = ReflectionUtils.getField(objectModel, fieldName);

    return field;
  }

  public static Object invokeConverter(Method method, Object hibernateEntity) {
    Extractor extractorAnn = method.getAnnotation(Extractor.class);
    Class<? extends AttributeExtractor> extractorClass = extractorAnn.value();
    AttributeExtractor extractorInstance = (AttributeExtractor) ReflectionUtils.newInstance(extractorClass);
    return extractorInstance.getAttributeValueEntity(hibernateEntity);
  }

  public static Object invokeConverter(Field field, Object hibernateEntity) {
    Attribute extractorAnn = field.getAnnotation(Attribute.class);
    Class<? extends AttributeEntityConverter> extractorClass = extractorAnn.converter();
    AttributeEntityConverter extractorInstance = (AttributeEntityConverter) ReflectionUtils.newInstance(extractorClass);
    return extractorInstance.convertToBusinessModel(hibernateEntity);
  }

  public static Object invokeConverterReverse(Field field, Object model) {
    Attribute extractorAnn = field.getAnnotation(Attribute.class);
    Class<? extends AttributeEntityConverter> extractorClass = extractorAnn.converter();
    AttributeEntityConverter extractorInstance = (AttributeEntityConverter) ReflectionUtils.newInstance(extractorClass);
    return extractorInstance.convertToEntityAttribute(model);
  }


}
