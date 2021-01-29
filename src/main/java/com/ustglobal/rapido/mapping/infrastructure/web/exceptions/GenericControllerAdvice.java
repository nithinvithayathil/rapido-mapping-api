package com.ustglobal.rapido.mapping.infrastructure.web.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ustglobal.rapido.mapping.domain.shared.DomainException;

@Configuration
@ControllerAdvice
public class GenericControllerAdvice {

	private static final Logger LOG = LoggerFactory.getLogger(GenericControllerAdvice.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ApplicationError> handleEntityNotFoundException(ResourceNotFoundException ex,
			WebRequest request) {

		LOG.error("Entity not found exception occurred.", ex);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(DomainException.class)
	public final ResponseEntity<ApplicationError> handleDomainException(DomainException ex, WebRequest request) {
		LOG.error("A domain exception occurred.", ex);
		return new ResponseEntity<>(new ApplicationError(ex), HttpStatus.CONFLICT);
	}

}
