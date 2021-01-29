package com.ustglobal.rapido.mapping.domain.shared;

public class DomainException extends RuntimeException {

	private static final long serialVersionUID = -8499915872414281297L;
	
	private DomainErrorCode errorCode;

	public DomainException(DomainErrorCode errorCode, Throwable throwable, Object... values) {
		super(String.format(errorCode.getMessage(), values), throwable);
		this.errorCode = errorCode;
	}

	public DomainErrorCode getErrorCode() {
		return errorCode;
	}

}
