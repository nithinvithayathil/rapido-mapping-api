package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd;

public class XSDParseException extends RuntimeException {
  public XSDParseException(String message, Exception ex){
    super(message, ex);
  }
  public XSDParseException(String message){
    super(message);
  }
}
