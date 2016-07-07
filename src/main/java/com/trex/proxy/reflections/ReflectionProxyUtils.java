package com.trex.proxy.reflections;



import com.trex.shared.annotations.CustomConverter;
import com.trex.shared.annotations.EntityReference;
import com.trex.shared.converters.AttributeEntityConverter;
import com.trex.shared.libraries.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
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

  public static Object customConvert(Field field, Object target) {

    CustomConverter annotation = field.getAnnotation(CustomConverter.class);
    Class<? extends AttributeEntityConverter> convertClass = annotation.convert();
    AttributeEntityConverter attributeEntityConverter =
        (AttributeEntityConverter) ReflectionUtils.newInstance(convertClass);

    return attributeEntityConverter.convertToBusinessModel(target);
  }

}
