package com.kanban.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kanban.project.domain.User;
import com.kanban.project.exceptions.UsernameAlreadyExistsException;
import com.kanban.project.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User saveUser(User newUser) {
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			//Username has to be unique(exception)
			newUser.setUsername(newUser.getUsername());
			//Make sure the password and confirmPassword match
			//We don't persist or show the confirmPassword
			return userRepository.save(newUser);	
		} catch (Exception e) {
			throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exist");
		}
	}
}
