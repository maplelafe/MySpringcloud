package com.ffb.beans;

import java.lang.annotation.*;

/**
 * <p>Project: framework-core</p>
 * <p>Function: The Interface FieldInfo.</p>
 * <p>Description: </p>
 * <p>Company: 税友软件集团股份有限公司</p>
 * 
 * @author wenzc
 */
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
