package com.kanban.project.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectNotFoundInAccountExceptionResponse {
	
	private String projectNotFoundInAccount;
	
	public ProjectNotFoundInAccountExceptionResponse(String projectNotFoundInAccount) {
		this.projectNotFoundInAccount = projectNotFoundInAccount;
	}

}
