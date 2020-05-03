package com.kanban.project.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameAlreadyExistsResponse {

	private String username;
	
	public UsernameAlreadyExistsResponse(String username) {
		this.username = username;
	}
}
