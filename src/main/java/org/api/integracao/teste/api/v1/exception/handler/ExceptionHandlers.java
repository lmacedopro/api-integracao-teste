package org.api.integracao.teste.api.v1.exception.handler;

import org.api.integracao.teste.api.v1.exception.ApiException;
import org.api.integracao.teste.api.v1.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

	// STATUS: 400
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<Object> apiHandler(ApiException e) {
		ExceptionDescriptor descriptor = new ExceptionDescriptor(e, HttpStatus.BAD_REQUEST);

		return ResponseEntity.status(descriptor.getStatus()).body(descriptor);
	}
	
	// STATUS: 401
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Object> unauthorizedHandler(UnauthorizedException e) {
		ExceptionDescriptor descriptor = new ExceptionDescriptor(e, HttpStatus.UNAUTHORIZED);

		return ResponseEntity.status(descriptor.getStatus()).body(descriptor);
	}

	// STATUS: 400
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> httpMessageNotReadableHandler(HttpMessageNotReadableException e) {
		ExceptionDescriptor descriptor = new ExceptionDescriptor(e, HttpStatus.BAD_REQUEST);

		return ResponseEntity.status(descriptor.getStatus()).body(descriptor);
	}

	// STATUS: 400
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> httpMissingServletRequestParameterHandler(MissingServletRequestParameterException e) {
		ExceptionDescriptor descriptor = new ExceptionDescriptor(e, HttpStatus.BAD_REQUEST);

		return ResponseEntity.status(descriptor.getStatus()).body(descriptor);
	}
	
	// STATUS: 415
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<Object> mediaTypeNotSupportedHandler(HttpMediaTypeNotSupportedException e) {
		ExceptionDescriptor descriptor = new ExceptionDescriptor(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE);

		return ResponseEntity.status(descriptor.getStatus()).body(descriptor);
	}

}
