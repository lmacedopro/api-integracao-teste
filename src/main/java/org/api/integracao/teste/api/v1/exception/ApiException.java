package org.api.integracao.teste.api.v1.exception;

public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 8327303938010913697L;

	public ApiException(String message) {
		super(message);
	}
}