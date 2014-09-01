package com.gda.dbgenerator.processors.elements;

/**
 * Created by gdaAquino on 5/22/14.
 */
public class FieldElement {

  private String fieldName;

  private String fieldType;

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

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("FieldElement{");
    sb.append("fieldName='").append(fieldName).append('\'');
    sb.append(", fieldType='").append(fieldType).append('\'');
    sb.append(", unique=").append(unique);
    sb.append(", notNull=").append(notNull);
    sb.append('}');
    return sb.toString();
  }
}
