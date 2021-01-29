package com.ustglobal.rapido.mapping.infrastructure.web.exceptions;

import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationError {

	private List<Error> errors;

	public ApplicationError() {
		this.errors = new ArrayList<>();
	}

	/**
	 * A convinient constructor to create an ApplicationError with only one message.
	 * 
	 * @param ex
	 *            Current exception
	 */
	public ApplicationError(DomainException ex) {
		this();
		this.addMessage(ex.getErrorCode().getCode(), ex.getMessage());
	}

	private void addMessage(int code, String message) {
		this.errors.add(new Error(code, message));
	}

	public List<Error> getErrors() {
		return errors;
	}

	public class Error {
		private int code;
		private String message;

		Error(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}
	}
}
