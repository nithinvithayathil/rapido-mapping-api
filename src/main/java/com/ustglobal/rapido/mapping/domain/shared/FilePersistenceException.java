package com.ustglobal.rapido.mapping.domain.shared;

public class FilePersistenceException extends RuntimeException {

  public FilePersistenceException(String message, Exception ex) {
    super(message, ex);
  }

  public FilePersistenceException(String message){
    super(message);
  }
}
