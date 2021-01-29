package com.ustglobal.rapido.mapping.domain.shared;

import java.util.Collection;
import org.apache.commons.lang3.Validate;

/**
 * Common validation methods for the domain
 */
public class DomainValidator {

  /**
   * Check if the given collection is empty
   * @param collection Collection to validate
   * @param errorCode The error code to be returned after validation
   * @param values Values to be passed on to customize the message
   * @param <T>
   * @return
   */
  public static <T extends Collection<?>> T notEmpty(T collection,
      DomainErrorCode errorCode, Object... values) {
    try {
      return Validate.notEmpty(collection);
    }
    catch (Exception ex){
      throw new DomainException(errorCode, ex, values);
    }
  }

  /**
   * Check if the given array is empty
   * @param array Array to validate
   * @param errorCode The error code to be returned after validation
   * @param values Values to be passed on to customize the message
   * @param <T>
   * @return
   */
  public static <T> T[] notEmpty(T[] array, DomainErrorCode errorCode, Object... values) {
    try {
      return Validate.notEmpty(array);
    }
    catch (Exception ex){
      throw new DomainException(errorCode, ex, values);
    }
  }

  /**
   * Checks if the given character sequence is null or empty
   * @param chars Character sequence to validate
   * @param errorCode The error code to be returned after validation
   * @param values Values to be passed on to customize the message
   * @param <T>
   * @return Validated character sequence
   * @throws DomainException
   */
  public static <T extends CharSequence> T notEmpty(T chars, DomainErrorCode errorCode, Object... values)
      throws DomainException {
    try {
      return Validate.notEmpty(chars);
    }
    catch (Exception ex){
      throw new DomainException(errorCode, ex, values);
    }
  }

  /**
   * Checks if given object is null
   * @param object Object to validate
   * @param errorCode The error code to be returned after validation
   * @param values Values to be passed on to customize the message
   * @param <T>
   * @return
   * @throws DomainException
   */
  public static <T> T notNull(T object, DomainErrorCode errorCode, Object... values) throws DomainException {
    try {
      return Validate.notNull(object);
    }
    catch (Exception ex){
      throw new DomainException(errorCode, ex, values);
    }
  }


  public static void isInstanceOf(Class<?> type, Object obj, DomainErrorCode errorCode) {
    try {
      Validate.isInstanceOf(type, obj);
    }
    catch (Exception ex){
      throw new DomainException(errorCode, ex);
    }
  }
}
