package com.ustglobal.rapido.mapping.infrastructure.web.exceptions;

public class ResourceNotFoundException extends Exception {

  public ResourceNotFoundException(String resourceName, Object identifier) {
    super(String.format("%s with identifier %s not found.", resourceName, identifier));

  }
}
