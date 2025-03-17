package org.api.integracao.teste.api.v1.exception.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;

public class ExceptionDescriptor {
	
	private String version;
	private int status;
	private LocalDateTime timestamp;
	private String message;
	
	public ExceptionDescriptor(Exception e, HttpStatus httpStatus) {
		this.version = "v1";
		this.status = httpStatus.value();
		this.timestamp = LocalDateTime.now();
		this.message = e.getMessage();
	}
	
	public String getTimestamp() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		return timestamp.format(formatter);
	}
	
	public String getVersion() {
		return version;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
