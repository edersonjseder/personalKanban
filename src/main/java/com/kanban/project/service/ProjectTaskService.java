package com.kanban.project.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kanban.project.domain.Backlog;
import com.kanban.project.domain.ProjectTask;
import com.kanban.project.exceptions.ProjectNotFoundException;
import com.kanban.project.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	private ProjectService projectService;
	
	/**
	 * Method that adds a project task to database
	 * 
	 * @param projectIdentifier the project identifier that links to the project it belongs to
	 * @param projectTask the project task object itself
	 * @param username 
	 * @return the project task object added
	 */
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

		//PTs to be added to a specific project, project != null, BL exists
		Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
		//Set the BL to PT
		projectTask.setBacklog(backlog);
		//We want our project sequence to be like this: IDPRO-1, IDPRO-2... IDPRO-N
		Integer backlogSequence = backlog.getPTSequence();
		//Update the BL sequence
		backlogSequence++;
		backlog.setPTSequence(backlogSequence);
		//Add sequence to Project Task
		projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);
		
		//Initial priority when priority null
		if(projectTask.getPriority() == null) {
			projectTask.setPriority(3);
		} else if(projectTask.getPriority() == 0) {
			projectTask.setPriority(3);
		}
		
		//Initial status when status null
		if(projectTask.getStatus() == null || projectTask.getStatus() == "") {
			projectTask.setStatus("TO_DO");
		}
				
		return projectTaskRepository.save(projectTask); 
	}

	/**
	 * It finds a stored project task by the backlog id which the project task belong to
	 * 
	 * @param backlog_id the backlog id parameter
	 * @return the project task object itself
	 */
	public Iterable<ProjectTask> findBacklogByProjectIdentifier(String project_identifier, String username) {
		
		projectService.findProjectByIdentifier(project_identifier, username);
				
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(project_identifier);
	}
	
	/**
	 * It finds the project task by project sequence
	 * 
	 * @param projectSequence the sequence parameter
	 * @return the project task object
	 */
	public ProjectTask findProjectTaskByProjectSequence(String projectIdentifier, String projectSequence, String username) {
		
		//Make sure we are searching on an existing backlog
		projectService.findProjectByIdentifier(projectIdentifier, username);
		
		//Make sure that our task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectSequence);
		
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project task '"+projectSequence+"' not found");
		}
		
		//Make sure that the backlog/project id in the path corresponds to the right project
		if(!projectTask.getProjectIdentifier().equals(projectIdentifier)) {
			throw new ProjectNotFoundException("Project task '"+projectSequence+"' doesn not exist in the project: '"+projectIdentifier);
		}
		
		return projectTask;
	}

	/**
	 * It updates the project task data existent in database
	 * 
	 * @param updatedProjectTask The project task object coming from UI
	 * @param projectIdentifier The parameter to find the project task by its identifier
	 * @param projectTaskSeq The parameter to find the project task by its sequence number
	 * @return The updated project task object
	 */
	public ProjectTask updateByProjectSequence(@Valid ProjectTask updatedProjectTask, String projectIdentifier, String projectTaskSeq, String username) {

		ProjectTask projectTask = findProjectTaskByProjectSequence(projectIdentifier, projectTaskSeq, username);
		
		projectTask = updatedProjectTask;
				
		return projectTaskRepository.save(projectTask);		
	}
	
	/**
	 * It deletes a project task object from database
	 * 
	 * @param projectIdentifier
	 * @param projectSequence
	 */
	public void deleteProjectTaskByProjectSequence(String projectIdentifier, String projectSequence, String username) {
		
		ProjectTask projectTask = findProjectTaskByProjectSequence(projectIdentifier, projectSequence, username);
		
		projectTaskRepository.delete(projectTask);
	}

}

