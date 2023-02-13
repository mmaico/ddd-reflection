package com.github.mmaico.clone.reflections;


import com.github.mmaico.clone.node.ChildNode;
import com.github.mmaico.clone.node.NodeFields;
import com.github.mmaico.shared.annotations.Attribute;
import com.github.mmaico.shared.annotations.Model;
import com.github.mmaico.shared.annotations.UpdateAttributes;
import com.github.mmaico.shared.converters.AttributeEntityConverter;
import com.github.mmaico.shared.libraries.ReflectionUtils;
import com.github.mmaico.shared.libraries.registers.PrimitiveTypeFields;
import com.google.common.collect.Lists;
import net.vidageek.mirror.dsl.Mirror;
import org.azeckoski.reflectutils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.mmaico.shared.libraries.ReflectionUtils.getField;
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
    List<Field> result = null;

    if (isModel(origin)) {
      result = getChildWithCustomConverter(origin);
    } else {
      result = getChildWithCustomConverter(destination);
    }

    for (Field field: result) {
      Attribute annotation = field.getAnnotation(Attribute.class);
      Class<? extends AttributeEntityConverter> convertClass = annotation.converter();
      AttributeEntityConverter attributeEntityConverter =
          (AttributeEntityConverter) ReflectionUtils.newInstance(convertClass);

      if (isModel(origin)) {
        Object valueConverted = attributeEntityConverter.convertToEntityAttribute(origin);
        if (isBlank(annotation.destinationName())) {
          ReflectionUtils.invokeSetter(destination, field.getName(), valueConverted);
        } else {
          ReflectionUtils.invokeSetter(destination, annotation.destinationName(), valueConverted);
        }
      } else {
        Object valueConverted = attributeEntityConverter.convertToBusinessModel(origin);
        ReflectionUtils.invokeSetter(destination, field.getName(), valueConverted);
      }

    }
  }

  public static List<Field> getChildWithCustomConverter(Object origin) {
    return new Mirror().on(origin.getClass()).reflectAll()
            .fields().matching(field -> {
                Attribute annotation = field.getAnnotation(Attribute.class);
                if (annotation != null && !annotation.converter().isInterface()) {
                  return Boolean.TRUE;
                } else {
                  return Boolean.FALSE;
                }
            });
  }

  //Refactory this method.
  public static List<ChildNode> getChildren(Object base) {
    Collection<String> fieldsToUpdate = getAttributesToUpdateDeclared(base);

    List<Field> fieldsFound = new Mirror().on(base.getClass())
            .reflectAll().fields()
            .matching(field -> !PrimitiveTypeFields.getInstance().contains(field.getType())
                    && field.getAnnotation(UpdateAttributes.class) == null);

    if (fieldsToUpdate.isEmpty()) {
        return fieldsFound.stream().map(field -> getChildNode(field))
              .collect(Collectors.toList());
    } else {
        return fieldsFound.stream().filter(field -> fieldsToUpdate.contains(field.getName()))
                .map(field -> getChildNode(field))
              .collect(Collectors.toList());
    }

  }

  private static ChildNode getChildNode(Field field) {
    Attribute annotation = field.getAnnotation(Attribute.class);
    if (annotation != null && annotation.converter().isInterface()) {
      return isBlank(annotation.destinationName())
              ? new ChildNode(field.getName(), field.getName())
              : new ChildNode(annotation.destinationName(), field.getName());
    } else {
      return new ChildNode(field.getName(), field.getName());
    }
  }

  public static Object getModel(Object origin, Object destination) {

    if (ReflectionUtils.hasAnnotation(origin.getClass(), Model.class)) {
        return origin;
    } else {
      return destination;
    }
  }

  public static Boolean isModel(Object object) {
    return ReflectionUtils.hasAnnotation(object.getClass(), Model.class);
  }

  public static Object newInstance(Object object, NodeFields nodeFields) {
    Optional<Field> fieldFound = null;
    if (isModel(object)) {
      fieldFound = getField(object, nodeFields.getFieldModelName());
    } else {
      fieldFound = getField(object, nodeFields.getField());
    }

    Optional<Class> genericClassCollection = ReflectionUtils.getGenericClassCollection(fieldFound.get());
    return ReflectionUtils.newInstance(genericClassCollection.get());
  }


  public static Object invokeGetter(Object object, NodeFields nodeFields) {
    try {
      if (isModel(object)) {
        return ReflectionUtils.invokeGetter(object, nodeFields.getFieldModelName());
      } else {
        return ReflectionUtils.invokeGetter(object, nodeFields.getField());
      }
    } catch (Exception e) {
      return null;
    }
  }

  public static void invokeSetter(Object target, NodeFields nodeFields, Object value) {
    try {
      if (isModel(target)) {
        ReflectUtils.getInstance().setFieldValue(target, nodeFields.getFieldModelName(), value);
      } else {
        ReflectUtils.getInstance().setFieldValue(target, nodeFields.getField(), value);
      }
    } catch (Exception e) {}
  }

  public static Object invokeGetter(Object object, String field) {
    try {
      return ReflectionUtils.invokeGetter(object, field);
    } catch (Exception e) {
      return null;
    }
  }


}
