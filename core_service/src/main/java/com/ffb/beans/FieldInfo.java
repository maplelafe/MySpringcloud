package com.ffb.beans;

import java.lang.annotation.*;


@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FieldInfo {

    /**
     * Field description.
     * 
     * @return the string
     */
    String fieldDescription();
}
