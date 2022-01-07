package com.github.mmaico.shared.libraries.registers;


import com.google.common.collect.Lists;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class PrimitiveTypeFields {

  private static final List<Class> types = Lists.newArrayList();

  static {

    types.add(Boolean.TYPE);
    types.add(Byte.TYPE);
    types.add(Character.TYPE);
    types.add(Double.TYPE);
    types.add(Float.TYPE);
    types.add(Integer.TYPE);
    types.add(Long.TYPE);
    types.add(Short.TYPE);

    types.add(BigDecimal.class);
    types.add(BigInteger.class);
    types.add(Boolean.class);
    types.add(Byte.class);
    types.add(Character.class);
    types.add(Double.class);
    types.add(Float.class);
    types.add(Integer.class);
    types.add(Long.class);
    types.add(Short.class);
    types.add(String.class);

    types.add(Class.class);
    types.add(java.util.Date.class);
    types.add(Calendar.class);
    types.add(File.class);
    types.add(java.sql.Date.class);
    types.add(java.sql.Time.class);
    types.add(Timestamp.class);
    types.add(URL.class);
  }

  public static PrimitiveTypeFields getInstance() {
    return new PrimitiveTypeFields();
  }

  public Boolean contains(Class clazz) {
    return types.contains(clazz);
  }


}
