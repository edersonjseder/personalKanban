package com.kanban.project.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectNotFoundExceptionResponse {
	
	private String projectNotFound;
	
	public ProjectNotFoundExceptionResponse(String projectIdentifier) {
		this.projectNotFound = projectIdentifier;
	}

}
