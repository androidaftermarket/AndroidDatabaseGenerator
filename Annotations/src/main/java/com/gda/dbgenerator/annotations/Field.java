package com.gda.dbgenerator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Gian Darren Azriel Aquino
 * @since 5/22/14
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Field {

  Type type();

  boolean unique() default false;

  boolean notNull() default false;

  public enum Type {

    TEXT("TEXT"), INTEGER("INTEGER");

    private String value;

    private Type(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
