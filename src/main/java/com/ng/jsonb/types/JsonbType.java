package com.ng.jsonb.types;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

@TypeDefs({@TypeDef(name = JsonbType.JSONB_TYPE, typeClass = JsonbType.class)})
public class JsonbType implements UserType, ParameterizedType {

  public static final String JSONB_TYPE = "jsonb";
  public static final String CLASS_TYPE = "classType";

  private static ObjectMapper mapper;

  private Class<?> classType;

  static {
    mapper = new ObjectMapper();
    mapper.configure(MapperFeature.USE_ANNOTATIONS, false);
  }

  @Override
  public Object nullSafeGet(
      ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
      throws HibernateException, SQLException {
    Object object = rs.getObject(names[0]);
    if (object instanceof PGobject) {
      String value = ((PGobject) object).getValue();
      return readObject(value);
    }
    return object;
  }

  @Override
  public void nullSafeSet(
      PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
      throws HibernateException, SQLException {

    if (value == null) {
      st.setNull(index, Types.OTHER);
      return;
    }

    String str = writeObject(value);
    PGobject pgObject = new PGobject();
    pgObject.setType(JSONB_TYPE);
    pgObject.setValue(str);
    st.setObject(index, pgObject, Types.OTHER);
  }

  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
    return areEquals(x, y);
  }

  @Override
  public Class returnedClass() {
    return classType;
  }

  @Override
  public int[] sqlTypes() {
    return new int[]{Types.JAVA_OBJECT};
  }

  @Override
  public Object deepCopy(Object originalValue) throws HibernateException {
    return safeCopy(originalValue);
  }

  @Override
  public Serializable disassemble(Object value) throws HibernateException {
    return writeObject(value);
  }

  @Override
  public Object assemble(Serializable cached, Object owner) throws HibernateException {
    return deepCopy(cached);
  }

  @Override
  public Object replace(Object original, Object target, Object owner) throws HibernateException {
    return deepCopy(original);
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public int hashCode(Object x) throws HibernateException {
    if (x == null) {
      return 0;
    }
    return x.hashCode();
  }

  @Override
  public void setParameterValues(Properties properties) {
    String classTypeName = properties.getProperty(CLASS_TYPE);
    try {
      this.classType = ReflectHelper.classForName(classTypeName, this.getClass());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("ClassType not found in context", e);
    }
  }

  private boolean areEquals(Object a, Object b) {
    mapper.configure(MapperFeature.USE_ANNOTATIONS, false);

    try {
      String strA = mapper.writeValueAsString(a);
      String strB = mapper.writeValueAsString(b);
      return Objects.equals(strA, strB);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Not possible to check if objects are equals", e);
    }
  }

  private Object safeCopy(Object o) {

    if (o == null) {
      return null;
    }

    try {
      String value = mapper.writeValueAsString(o);
      return mapper.readValue(value, o.getClass());
    } catch (IOException e) {
      throw new IllegalArgumentException("Not possible to create a safe copy of jsonb object", e);
    }
  }

  private Object readObject(String str) {
    try {
      return mapper.readValue(str, classType);
    } catch (IOException e) {
      throw new IllegalArgumentException("Not possible read the object to the class", e);
    }
  }

  private String writeObject(Object obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Not possible convert object to JSON", e);
    }
  }
}
