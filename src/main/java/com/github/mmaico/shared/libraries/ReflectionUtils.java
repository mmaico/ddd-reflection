package com.github.mmaico.shared.libraries;


import com.github.mmaico.shared.libraries.concurrent.ConcurrentReferenceHashMap;
import com.github.mmaico.shared.libraries.registers.PrimitiveTypeFields;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.list.dsl.MirrorList;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.azeckoski.reflectutils.ReflectUtils;


import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;


public class ReflectionUtils {

  /**
   * Cache for {@link Class#getDeclaredMethods()}, allowing for fast iteration.
   */
  private static final Map<Class<?>, Method[]> declaredMethodsCache =
      new ConcurrentReferenceHashMap<>(256);

  private static final Map<Class<?>, List<Field>> declaredFieldsCache =
          new ConcurrentReferenceHashMap<>(256);

  private static final Map<Class<?>, Map<String, Optional<Field>>> declaredFieldCache =
          new ConcurrentReferenceHashMap<>(256);

  private static final Map<String, Optional<Class>> classCache = new ConcurrentReferenceHashMap<>(256);

  private static final String GETTER_PREFIX = "get";


  public static List<Field> getFields(Object base, Collection<String> fieldsToUpdate) {
    if (fieldsToUpdate.isEmpty()) {
      return getFields(base);
    } else {
      return getFields(base).stream()
              .filter(field -> fieldsToUpdate.contains(field.getName())).collect(Collectors.toList());
    }
  }

  public static List<Field> getFields(Object base) {

    if (declaredFieldsCache.get(base.getClass()) == null) {
      MirrorList<Field> fields = new Mirror().on(base.getClass())
              .reflectAll().fields()
              .matching(field -> PrimitiveTypeFields.getInstance().contains(field.getType()));
      declaredFieldsCache.put(base.getClass(), fields);
      return fields;
    }

    return declaredFieldsCache.get(base.getClass());
  }

  public static Optional<Field> getField(Object base, String fieldName, Class annotation) {
    return new Mirror().on(base.getClass())
        .reflectAll().fields()
        .matching(field -> field.getAnnotation(annotation) != null && field.getName().equals(fieldName))
        .stream().findFirst();
  }

  public static Optional<Field> getField(Object base, Class annotation) {
    return new Mirror().on(base.getClass())
            .reflectAll().fields()
            .matching(field -> field.getAnnotation(annotation) != null)
            .stream().findFirst();
  }

  public static Optional<Field> getField(Object base, String fieldName) {

    if (declaredFieldCache.containsKey(base.getClass())
            && declaredFieldCache.get(base.getClass()).containsKey(fieldName)) {

      return declaredFieldCache.get(base.getClass()).get(fieldName);
    } else {

      Optional<Field> fieldFound = new Mirror().on(base.getClass())
              .reflectAll().fields()
              .matching(field -> field.getName().equals(fieldName))
              .stream().findFirst();

      if (declaredFieldCache.containsKey(base.getClass())) {
        declaredFieldCache.get(base.getClass()).put(fieldName, fieldFound);
      } else {
        Map<String, Optional<Field>> fieldClass = new HashMap<>();
        fieldClass.put(fieldName, fieldFound);

        declaredFieldCache.put(base.getClass(), fieldClass);
      }

      return fieldFound;
    }

  }

  public static Optional<Field> getFieldByGetOrSet(Object objectModel, String originMethodName) {
    String fieldName = StringUtils.uncapitalize(originMethodName.replaceAll("(get|set)", ""));

    Optional<Field> field = ReflectionUtils.getField(objectModel, fieldName);

    return field;
  }

  public static String getFieldByGetOrSet(Method method) {
    return StringUtils.uncapitalize(method.getName().replaceAll("(get|set)", ""));
  }

  public static Object invokeGetter(Object target, Field field) {
    return invokeGetter(target, field.getName());
  }

  public static Object invokeSafeGetter(Object target, Field field) {
    return invokeSafeGetter(target, field.getName());
  }

  public static Object invokeSafeGetter(Object target, String name) {
    String getterMethodName = name;
    if (!name.startsWith(GETTER_PREFIX)) {
      getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
    }
    Method method = findMethod(target.getClass(), getterMethodName);
    if (method == null && !getterMethodName.equals(name)) {
      getterMethodName = name;
      method = findMethod(target.getClass(), getterMethodName);
    }

    if (method == null) {
      return target;
    }

    makeAccessible(method);
    return invokeMethod(method, target);
  }

  public static Object invokeGetter(Object target, String name) {

    String getterMethodName = name;
    if (!name.startsWith(GETTER_PREFIX)) {
      getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
    }
    Method method = findMethod(target.getClass(), getterMethodName);
    if (method == null && !getterMethodName.equals(name)) {
      getterMethodName = name;
      method = findMethod(target.getClass(), getterMethodName);
    }
    if (method == null) {
      throw new IllegalArgumentException("Could not find getter method [" + getterMethodName + "] on target ["
          + target + "]");
    }

    makeAccessible(method);
    return invokeMethod(method, target);
  }

  public static Optional<Class> createClass(String className) {
    try {
      if (classCache.containsKey(className)) {
        return classCache.get(className);
      } else {
        Optional<Class> aClass = Optional.ofNullable(Class.forName(className));
        classCache.put(className, aClass);
        return aClass;
      }

    } catch (ClassNotFoundException e) {
      return Optional.empty();
    }
  }

  public static Object newInstance(Class clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void invokeSetter(Object target, Field field, Object value) {
    invokeSetter(target, field.getName(), value);
  }

  public static void invokeSetter(Object target, String fieldName, Object value) {
    try {
      ReflectUtils.getInstance().setFieldValue(target, fieldName, value);
    } catch (Exception e) {

    }
  }

  public static boolean hasAnnotation(Class target, Class annotationClazz) {

    Object object = new Mirror().on(target).reflect()
        .annotation(annotationClazz).atClass();

    return object != null;
  }

  public static Object getValue(Object target, Field field) {

    makeAccessible(field);
    try {
      return field.get(target);
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException("Error when trying to retrieve the value [" + field.getName() + "] on target ["
          + target.getClass() + "]");
    }

  }

  public static Optional<Class> getGenericClassCollection(Field field) {
    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
    Class<?> collectionType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
    return createClass(collectionType.getName());
  }

  //Copied methods of ReflectionUtils Spring Commons

  /**
   * Attempt to find a {@link Method} on the supplied class with the supplied name
   * and parameter types. Searches all superclasses up to {@code Object}.
   * <p>Returns {@code null} if no {@link Method} can be found.
   *
   * @param clazz      the class to introspect
   * @param name       the name of the method
   * @param paramTypes the parameter types of the method
   *                   (may be {@code null} to indicate any signature)
   * @return the Method object, or {@code null} if none found
   */
  public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
    Assert.notNull(clazz, "Class must not be null");
    Assert.notNull(name, "Method name must not be null");
    Class<?> searchType = clazz;
    while (searchType != null) {
      Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
      for (Method method : methods) {
        if (name.equals(method.getName()) &&
            (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
          return method;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }

  /**
   * This variant retrieves {@link Class#getDeclaredMethods()} from a local cache
   * in order to avoid the JVM's SecurityManager check and defensive array copying.
   */
  private static Method[] getDeclaredMethods(Class<?> clazz) {
    Method[] result = declaredMethodsCache.get(clazz);
    if (result == null) {
      result = clazz.getDeclaredMethods();
      declaredMethodsCache.put(clazz, result);
    }
    return result;
  }


  /**
   * Make the given field accessible, explicitly setting it accessible if
   * necessary. The {@code setAccessible(true)} method is only called
   * when actually necessary, to avoid unnecessary conflicts with a JVM
   * SecurityManager (if active).
   *
   * @param field the field to make accessible
   * @see java.lang.reflect.Field#setAccessible
   */
  public static void makeAccessible(Field field) {
    if ((!Modifier.isPublic(field.getModifiers()) ||
        !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
        Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
      field.setAccessible(true);
    }
  }

  /**
   * Make the given method accessible, explicitly setting it accessible if
   * necessary. The {@code setAccessible(true)} method is only called
   * when actually necessary, to avoid unnecessary conflicts with a JVM
   * SecurityManager (if active).
   *
   * @param method the method to make accessible
   * @see java.lang.reflect.Method#setAccessible
   */
  public static void makeAccessible(Method method) {
    if ((!Modifier.isPublic(method.getModifiers()) ||
        !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
      method.setAccessible(true);
    }
  }

  /**
   * Invoke the specified {@link Method} against the supplied target object with no arguments.
   * The target object can be {@code null} when invoking a static {@link Method}.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
   *
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @return the invocation result, if any
   * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
   */
  public static Object invokeMethod(Method method, Object target) {
    return invokeMethod(method, target, new Object[0]);
  }

  /**
   * Invoke the specified {@link Method} against the supplied target object with the
   * supplied arguments. The target object can be {@code null} when invoking a
   * static {@link Method}.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
   *
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @param args   the invocation arguments (may be {@code null})
   * @return the invocation result, if any
   */
  public static Object invokeMethod(Method method, Object target, Object... args) {
    try {
      return method.invoke(target, args);
    } catch (Exception ex) {
      handleReflectionException(ex);
    }
    throw new IllegalStateException("Should never get here");
  }

  /**
   * Handle the given reflection exception. Should only be called if no
   * checked exception is expected to be thrown by the target method.
   * <p>Throws the underlying RuntimeException or Error in case of an
   * InvocationTargetException with such a root cause. Throws an
   * IllegalStateException with an appropriate message else.
   *
   * @param ex the reflection exception to handle
   */
  public static void handleReflectionException(Exception ex) {
    if (ex instanceof NoSuchMethodException) {
      throw new IllegalStateException("Method not found: " + ex.getMessage());
    }
    if (ex instanceof IllegalAccessException) {
      throw new IllegalStateException("Could not access method: " + ex.getMessage());
    }
    if (ex instanceof InvocationTargetException) {
      throw new IllegalStateException("Could not access method: " + ex.getMessage());
    }
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    throw new UndeclaredThrowableException(ex);
  }

  public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
    final ArrayList methods = new ArrayList(32);
    doWithMethods(leafClass, method -> methods.add(method));
    return (Method[]) methods.toArray(new Method[methods.size()]);
  }

  public static void doWithMethods(Class<?> clazz, ReflectionUtils.MethodCallback mc) {
    doWithMethods(clazz, mc, (MethodFilter) null);
  }

  public static void doWithMethods(Class<?> clazz, ReflectionUtils.MethodCallback mc, ReflectionUtils.MethodFilter mf) {
    Method[] methods = getDeclaredMethods(clazz);
    Method[] var4 = methods;
    int var5 = methods.length;

    int var6;
    for (var6 = 0; var6 < var5; ++var6) {
      Method superIfc = var4[var6];
      if (mf == null || mf.matches(superIfc)) {
        try {
          mc.doWith(superIfc);
        } catch (IllegalAccessException var9) {
          throw new IllegalStateException("Not allowed to access method \'" + superIfc.getName() + "\': " + var9);
        }
      }
    }

    if (clazz.getSuperclass() != null) {
      doWithMethods(clazz.getSuperclass(), mc, mf);
    } else if (clazz.isInterface()) {
      Class[] var10 = clazz.getInterfaces();
      var5 = var10.length;

      for (var6 = 0; var6 < var5; ++var6) {
        Class var11 = var10[var6];
        doWithMethods(var11, mc, mf);
      }
    }

  }

  /**
   * Action to take on each method.
   */
  public interface MethodCallback {

    /**
     * Perform an operation using the given method.
     *
     * @param method the method to operate on
     */
    void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
  }

  /**
   * Callback optionally used to filter methods to be operated on by a method callback.
   */
  public interface MethodFilter {

    /**
     * Determine whether the given method matches.
     * @param method the method to check
     */
    boolean matches(Method method);
  }

}
