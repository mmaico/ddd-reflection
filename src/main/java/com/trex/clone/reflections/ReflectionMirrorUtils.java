package com.trex.clone.reflections;



import com.google.common.collect.Lists;
import com.trex.shared.annotations.EntityReference;
import com.trex.clone.node.ChildNode;
import com.trex.shared.libraries.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ReflectionMirrorUtils {


  public static void mergePrimitiveAttributes(Object base, Object target) {
    List<Field> fields = ReflectionUtils.getFields(base);
    fields.stream()
          .forEach(field -> ReflectionUtils.invokeSetter(target, field, ReflectionUtils.invokeSafeGetter(base, field)));
  }

  public static List<ChildNode> getReferenceFields(Object base) {
    return ReflectionUtils.getValues(base, Lists.newArrayList(EntityReference.class));
  }

  public static Object newInstanceByReference(Object origin) {
    Optional<EntityReference> annotation = Optional.ofNullable(origin.getClass().getAnnotation(EntityReference.class));

    if (annotation.isPresent()) {
      return ReflectionUtils.newInstance(annotation.get().value());
    }

    return null;
  }

  public static Object invokeGetter(Object object, Field field) {
    EntityReference annotation = field.getAnnotation(EntityReference.class);
    if (isBlank(annotation.fieldName())) {
      return ReflectionUtils.invokeSafeGetter(object, field.getName());
    } else {
      return ReflectionUtils.invokeSafeGetter(object, annotation.fieldName());
    }
  }

  public static Optional<Field> getDestField(Object objectDest, Field fieldOrigin) {
    EntityReference annotation = fieldOrigin.getAnnotation(EntityReference.class);
    if (objectDest == null) {
      return Optional.empty();
    }

    if (isBlank(annotation.fieldName())) {
      return ReflectionUtils.getField(objectDest, fieldOrigin.getName());
    } else {
      return ReflectionUtils.getField(objectDest, annotation.fieldName());
    }
  }

  public static String getPropertyName(Field field) {
    EntityReference annotation = field.getAnnotation(EntityReference.class);
    if (isBlank(annotation.fieldName())) {
      return field.getName();
    } else {
      return annotation.fieldName();
    }
  }
}
