package org.api.integracao.teste.api.v1.exception;

public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 8327303938010913697L;

	public UnauthorizedException(String message) {
		super(message);
	}
}