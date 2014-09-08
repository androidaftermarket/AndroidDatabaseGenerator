package com.gda.dbgenerator.processors.elements;

/**
 * Created by gdaAquino on 5/22/14.
 */
public class FieldElement {

  private String fieldName;

  private String fieldType;

  private String declaredFieldType;

  private boolean unique;

  private boolean notNull;



  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldType() {
    return fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public String getDeclaredFieldType() {
    return declaredFieldType;
  }

  public void setDeclaredFieldType(String declaredFieldType) {
    this.declaredFieldType = declaredFieldType;
  }

  public boolean isUnique() {
    return unique;
  }

  public void setUnique(boolean unique) {
    this.unique = unique;
  }

  public boolean isNotNull() {
    return notNull;
  }

  public void setNotNull(boolean notNull) {
    this.notNull = notNull;
  }

  @Override public String toString() {
    return "FieldElement{" +
        "declaredFieldType=" + declaredFieldType +
        ", notNull=" + notNull +
        ", unique=" + unique +
        ", fieldType='" + fieldType + '\'' +
        ", fieldName='" + fieldName + '\'' +
        '}';
  }
}
