package com.kanban.project.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidLoginResponseException {

	private String username;
	private String password;

	public InvalidLoginResponseException() {
		this.username = "Invalid username";
		this.password = "Invalid password";
	}
	
}
