package com.gda.dbgenerator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gdaAquino on 5/22/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface DbConfig {

    String databaseName();

    String authority() default "authority";

    int databaseVersion() default 0;

    boolean useContentProvider() default false;

}
