package com.trex.proxy.reflections;



import com.trex.proxy.extractors.AttributeExtractor;
import com.trex.shared.annotations.Attribute;
import com.trex.shared.annotations.CustomConverter;
import com.trex.shared.annotations.EntityReference;
import com.trex.shared.annotations.Extractor;
import com.trex.shared.converters.AttributeEntityConverter;
import com.trex.shared.libraries.ReflectionUtils;
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
    if (fieldFound.getAnnotation(EntityReference.class) != null) {
      EntityReference referenceEntityFound = fieldFound.getAnnotation(EntityReference.class);
      return isBlank(referenceEntityFound.fieldName()) ? fieldName : referenceEntityFound.fieldName();
    } else if (fieldFound.getAnnotation(CustomConverter.class) != null) {
      CustomConverter customConverterFound = fieldFound.getAnnotation(CustomConverter.class);
      return isBlank(customConverterFound.fieldName()) ? fieldName : customConverterFound.fieldName();
    }

    return fieldName;
  }

  public static Field getFieldByMethodName(Object target, String methodName) {
    String fieldName = StringUtils.uncapitalize(methodName.replaceAll("(get|set)", ""));

    Optional<Field> field = ReflectionUtils.getField(target, fieldName);
    return field.get();
  }

  public static Boolean hasCustomConverter(Object objectModel, String methodName) {
    Field fieldObjectModel = ReflectionProxyUtils.getFieldByMethodName(objectModel, methodName);
    return fieldObjectModel.getAnnotation(CustomConverter.class) != null;
  }

  public static Object invokeExtractor(Method method, Object hibernateEntity) {
    Extractor extractorAnn = method.getAnnotation(Extractor.class);
    Class<? extends AttributeExtractor> extractorClass = extractorAnn.value();
    AttributeExtractor extractorInstance = (AttributeExtractor) ReflectionUtils.newInstance(extractorClass);
    return extractorInstance.getAttributeValueEntity(hibernateEntity);
  }

  public static Object invokeExtractor(Field field, Object hibernateEntity) {
    Attribute extractorAnn = field.getAnnotation(Attribute.class);
    Class<? extends AttributeEntityConverter> extractorClass = extractorAnn.converter();
    AttributeEntityConverter extractorInstance = (AttributeEntityConverter) ReflectionUtils.newInstance(extractorClass);
    return extractorInstance.convertToBusinessModel(hibernateEntity);
  }

  public static Object invokeExtractorReverse(Method method, Object hibernateEntity) {
    Extractor extractorAnn = method.getAnnotation(Extractor.class);
    Class<? extends AttributeExtractor> extractorClass = extractorAnn.value();
    AttributeExtractor extractorInstance = (AttributeExtractor) ReflectionUtils.newInstance(extractorClass);
    return extractorInstance.getAttributeValueModel(hibernateEntity);
  }

  public static Object invokeCustomConverter(Object hibernateEntity, Field entityField, Field fieldObjectModel) {

    CustomConverter annotation = fieldObjectModel.getAnnotation(CustomConverter.class);
    Class<? extends AttributeEntityConverter> convertClass = annotation.convert();
    AttributeEntityConverter attributeEntityConverter =
        (AttributeEntityConverter) ReflectionUtils.newInstance(convertClass);

    Object result = ReflectionUtils.getValue(hibernateEntity, entityField);

    return attributeEntityConverter.convertToBusinessModel(result);
  }

  public static Object invokeCustomConverterReverse(Field fieldObjectModel, Object valueToConvert) {

    CustomConverter annotation = fieldObjectModel.getAnnotation(CustomConverter.class);
    Class<? extends AttributeEntityConverter> convertClass = annotation.convert();
    AttributeEntityConverter attributeEntityConverter =
            (AttributeEntityConverter) ReflectionUtils.newInstance(convertClass);

    return attributeEntityConverter.convertToEntityAttribute(valueToConvert);
  }

}
