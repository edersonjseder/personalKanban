package com.kanban.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectIdException extends RuntimeException {
	
	private static final long serialVersionUID = 8132295630191495469L;

	public ProjectIdException(String message) {
		super(message);
	}

}
