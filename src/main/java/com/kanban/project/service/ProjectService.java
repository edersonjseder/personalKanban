package com.kanban.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kanban.project.domain.Backlog;
import com.kanban.project.domain.Project;
import com.kanban.project.domain.User;
import com.kanban.project.exceptions.ProjectIdException;
import com.kanban.project.exceptions.ProjectNotFoundException;
import com.kanban.project.exceptions.ProjectNotFoundInAccountException;
import com.kanban.project.repository.BacklogRepository;
import com.kanban.project.repository.ProjectRepository;
import com.kanban.project.repository.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Project saveOrUpdate(Project project, String username) {
		
		if (project.getId() != null) {
			Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
			
			if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
				throw new ProjectNotFoundInAccountException("Project not found in your account");
			} else if (existingProject == null) {
				throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier() + "' cannot be updated because it doesn't exist");
			}
		}
		
		try {
			
			User user = userRepository.findByUsername(username);
			
			project.setUser(user);
	
			project.setProjectLeader(user.getUsername());
			
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			if(project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier());
			}
			
			if(project.getId() != null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier()));
			}
			
			return projectRepository.save(project);
			
		}catch (Exception e) {
			throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
	
		}
		
	}
	
	public Project findProjectByIdentifier(String projectId, String username) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {	
			throw new ProjectIdException("Project ID '" + projectId + "' does not exist");
		}
		
		if (!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundInAccountException("Project not found in your account");
		}
		
		return project;
		
	}
	
	public Iterable<Project> findAllProjects(String username) {
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProjectByIdentifier(String projectId, String username) {
		
		projectRepository.delete(findProjectByIdentifier(projectId, username));
		
	}
	
}
