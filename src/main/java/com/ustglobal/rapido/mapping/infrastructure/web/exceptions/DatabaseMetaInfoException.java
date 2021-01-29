package com.ustglobal.rapido.mapping.infrastructure.web.exceptions;

public class DatabaseMetaInfoException extends RuntimeException {

  public DatabaseMetaInfoException(String dbConfigId, String methodName, Exception ex){
    super(String.format("Error while calling %s for db configuration with id %s", methodName, dbConfigId), ex);
  }
}
