package com.trex.clone.reflections;



import com.google.common.collect.Lists;
import com.trex.shared.annotations.CustomConverter;
import com.trex.shared.annotations.EntityReference;
import com.trex.clone.node.ChildNode;
import com.trex.shared.annotations.UpdateAttributes;
import com.trex.shared.converters.AttributeEntityConverter;
import com.trex.shared.libraries.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ReflectionCloneUtils {


  public static void mergePrimitiveAttributes(Object origin, Object destination) {
    Collection<String> fieldsToUpdate = getAttributesToUpdateDeclared(origin);

    List<Field> fields = ReflectionUtils.getFields(origin, fieldsToUpdate);
    fields.stream()
          .forEach(field -> ReflectionUtils.invokeSetter(destination, field, ReflectionUtils.invokeSafeGetter(origin, field)));
  }

  private static Collection<String> getAttributesToUpdateDeclared(Object origin) {
    Optional<Field> updateAttributes = ReflectionUtils.getField(origin, UpdateAttributes.class);
    Collection<String> fieldsToUpdate = Lists.newArrayList();

    if (updateAttributes.isPresent()) {
      Object object = ReflectionUtils.invokeSafeGetter(origin, updateAttributes.get());
      if (object instanceof Collection) {
        ((Collection) object).stream().forEach(item -> fieldsToUpdate.add(item.toString()));
      }
    }

    return fieldsToUpdate;
  }

  public static void mergeAttrWithCustomConverter(Object origin, Object destination) {
    List<ChildNode> result = ReflectionUtils.getValues(origin, Lists.newArrayList(CustomConverter.class));

    for (ChildNode node: result) {
      CustomConverter annotation = node.getField().getAnnotation(CustomConverter.class);
      Class<? extends AttributeEntityConverter> convertClass = annotation.convert();
      AttributeEntityConverter attributeEntityConverter =
          (AttributeEntityConverter) ReflectionUtils.newInstance(convertClass);

      Object valueConverted = attributeEntityConverter.convertToEntityAttribute(node.getObject());

      if (StringUtils.isBlank(annotation.fieldName())) {
        ReflectionUtils.invokeSetter(destination, node.getField().getName(), valueConverted);
      } else {
        ReflectionUtils.invokeSetter(destination, annotation.fieldName(), valueConverted);
      }
    }
  }

  public static List<ChildNode> getReferenceFields(Object base) {
    Collection<String> fieldsToUpdate = getAttributesToUpdateDeclared(base);

    List<ChildNode> childNodes = ReflectionUtils.getValues(base, Lists.newArrayList(EntityReference.class));

    if (fieldsToUpdate.isEmpty()) {
      return childNodes;
    } else {
      return childNodes.stream().filter(node -> fieldsToUpdate.contains(node.getField().getName()))
              .collect(Collectors.toList());
    }

  }

  public static Object newInstanceByReference(Object origin) {

    if (origin == null) return null;

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
