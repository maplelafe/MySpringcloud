package com.ffb.beans.converter;

import org.apache.commons.beanutils.converters.NumberConverter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 将String转换成BigDecamall.
 * 
 * @author gong
 */
public class BigDicimalConverter extends NumberConverter {

    /**
     * Instantiates a new big dicimal converter.
     * 
     * @param allowDecimals
     *            the allow decimals
     */
    public BigDicimalConverter(boolean allowDecimals) {
        super(allowDecimals);
    }

    /**
     * Instantiates a new big dicimal converter.
     * 
     * @param allowDecimals
     *            the allow decimals
     * @param defaultValue
     *            the default value
     */
    public BigDicimalConverter(boolean allowDecimals, Object defaultValue) {
        super(allowDecimals, defaultValue);
    }

    /* (non-Javadoc)
     * @see org.apache.commons.beanutils.converters.AbstractConverter#getDefaultType()
     */
    @Override
    protected Class<?> getDefaultType() {
        return BigDecimal.class;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.beanutils.converters.NumberConverter#convertToType(java.lang.Class, java.lang.Object)
     */
    @Override
    protected <T> T convertToType(Class<T> targetType, Object value) throws Throwable {
        if (value == null || (value instanceof String && StringUtils.isEmpty(value))) {
            value = "0";// NOSONAR
        }
        return super.convertToType(targetType, value);
    }

}
