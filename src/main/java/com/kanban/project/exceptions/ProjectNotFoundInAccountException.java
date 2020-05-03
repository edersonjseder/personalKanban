package com.kanban.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectNotFoundInAccountException extends RuntimeException {

	private static final long serialVersionUID = 4932364658350349009L;

	public ProjectNotFoundInAccountException(String message) {
		super(message);
	}
	
}
