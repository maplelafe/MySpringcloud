package com.ffb.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SuppressWarnings("serial")
public final class EnumHelper implements Serializable {

    /**
     * The Constant GET_CODE_METHOD.
     */
    private static final String GET_CODE_METHOD = "getCode";

    /**
     * The Constant GET_LABEL_METHOD.
     */
    private static final String GET_LABEL_METHOD = "getText";

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EnumHelper.class);

    /**
     * Instantiates a new enum helper.
     */
    private EnumHelper() {

    }

    /**
     * Gets an mutable list of enumerations defined in the given Enum.
     * 
     * @param <T>
     *            subclass of Enum
     * @param clazz
     *            the class instance
     * @return a list of objects of type T
     */
    public static <T extends Enum<T>> List<T> inspectConstants(final Class<T> clazz) {
        return new ArrayList<T>(Arrays.asList(clazz.getEnumConstants()));
    }

    /**
     * Gets an mutable list of enumerations defined in the given Enum.
     * 
     * @param <T>
     *            subclass of Enum
     * @param clazz
     *            the class instance
     * @param containsNull
     *            the contains null
     * @return a list of objects of type T
     */
    public static <T extends Enum<T>> List<T> inspectConstants(final Class<T> clazz, boolean containsNull) {
        List<T> list = new ArrayList<T>(Arrays.asList(clazz.getEnumConstants()));
        if (!containsNull) {
            list.remove(0);
        }
        return list;
    }

    /**
     * Translates code into its corresponding enum instance.
     * <p>
     * NOTE: To make this function work, please DO implement the following
     * method in your Enum class:
     * 
     * <pre>
     * public String getCode() {
     * // return the unique code
     * return &quot;CODE&quot;;
     * }
     * </pre>
     * 
     * </p>
     * 
     * @param <T>
     *            type of enum
     * @param clazz
     *            the type of enum instance expected
     * @param code
     *            the unique customized code for the enum instance. Usually, the
     *            code is the used in underlying database table.
     * @return an instance of type T, or null if the code is not defined
     */
    public static <T extends Enum<T>> T translate(final Class<T> clazz, final String code) {
        try {
            final Method m = clazz.getDeclaredMethod(GET_CODE_METHOD);
            for (T t : inspectConstants(clazz)) {
                if (code.equals(m.invoke(t))) {
                    return t;
                }
            }
        } catch (SecurityException e) {
            LOGGER.error("failed to translate label {} into object of type {}", code, clazz, e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("failed to translate label {} into object of type {}", code, clazz, e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("failed to translate label {} into object of type {}", code, clazz, e);
        } catch (IllegalAccessException e) {
            LOGGER.error("failed to translate label {} into object of type {}", code, clazz, e);
        } catch (InvocationTargetException e) {
            LOGGER.error("failed to translate label {} into object of type {}", code, clazz, e);
        }
        LOGGER.debug("failed to translate code {} into object of type {}", code, clazz);
        return null;
    }

    /**
     * Retrieve code value of certain Enum instance, this method is null-safe.
     * i.e. returns null if input instance is null.
     * 
     * @param <T>
     *            the generic type
     * @param obj
     *            the obj
     * @return code
     */
    public static <T extends Enum<T>> String getCode(final T obj) {
        if (obj == null) {
            return null;
        }

        try {
            Class<?> clazz = obj.getClass();
            Method m = clazz.getDeclaredMethod(GET_CODE_METHOD);
            return m.invoke(obj).toString();
        } catch (SecurityException e) {
            LOGGER.error("", e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("", e);
        } catch (InvocationTargetException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /**
     * Translates label into its corresponding enum instance.
     * 
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param label
     *            the label
     * @return the t
     */
    public static <T extends Enum<T>> T translateByLabel(final Class<T> clazz, final String label) {
        try {
            final Method m = clazz.getDeclaredMethod(GET_LABEL_METHOD);
            for (T t : inspectConstants(clazz)) {
                if (label.equals(m.invoke(t))) {
                    return t;
                }
            }
        } catch (SecurityException e) {
            LOGGER.error("failed to translate label {} into object of type {}", label, clazz, e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("failed to translate label {} into object of type {}", label, clazz, e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("failed to translate label {} into object of type {}", label, clazz, e);
        } catch (IllegalAccessException e) {
            LOGGER.error("failed to translate label {} into object of type {}", label, clazz, e);
        } catch (InvocationTargetException e) {
            LOGGER.error("failed to translate label {} into object of type {}", label, clazz, e);
        }
        LOGGER.debug("failed to translate label {} into object of type {}", label, clazz);
        return null;
    }

}
