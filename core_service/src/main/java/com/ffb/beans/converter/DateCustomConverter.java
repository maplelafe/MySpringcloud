package com.ffb.beans.converter;

import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>Project: framework-core</p>
 * <p>Function: The Class DateCustomConverter.</p>
 * <p>Description: </p>
 * <p>Company: 税友软件集团股份有限公司</p>
 * 
 * @author sbf-dev
 * @version 2.0
 */
public class DateCustomConverter extends DateTimeConverter {

    /**
     * Instantiates a new date custom converter.
     */
    public DateCustomConverter() {
        this.setUseLocaleFormat(true);
        String[] patterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss, yyyy-MM-dd HH:mm" };
        this.setPatterns(patterns);
    }

    /* (non-Javadoc)
     * @see org.apache.commons.beanutils.converters.AbstractConverter#convert(java.lang.Class, java.lang.Object)
     */
    @Override
    public <T> T convert(Class<T> type, Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return super.convert(type, value);
        }
        String strValue = (String) value;
        if (value instanceof String && (StringUtils.isEmpty(strValue) || StringUtils.isEmpty(strValue.trim()))) {
            return null;
        }
        return super.convert(type, strValue);
    }

    /* (non-Javadoc)
     * @see org.apache.commons.beanutils.converters.AbstractConverter#getDefaultType()
     */
    @Override
    protected Class<?> getDefaultType() {
        return Date.class;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.beanutils.converters.DateTimeConverter#convertToType(java.lang.Class, java.lang.Object)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected Object convertToType(Class arg0, Object arg1) throws Exception {
        if (arg1 == null) {
            return null;
        }
        String value = arg1.toString().trim();
        if (value.length() == 0) {
            return null;
        }
        return super.convertToType(arg0, arg1);
    }

}
