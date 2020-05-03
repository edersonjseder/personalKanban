package com.kanban.project.web;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.project.domain.ProjectTask;
import com.kanban.project.service.MapValidationErrorService;
import com.kanban.project.service.ProjectTaskService;

@RestController
@RequestMapping("/api/projectTask")
@CrossOrigin
public class ProjectTaskController {

	@Autowired
	private ProjectTaskService projectTaskService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@PostMapping("/{project_identifier}")
	public ResponseEntity<?> addProjectTasktoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result, 
													 @PathVariable String project_identifier, Principal principal) {
		
		ResponseEntity<?> errorMap = mapValidationErrorService.getMapValidationService(result);
		if(errorMap != null) return errorMap;		
		
		ProjectTask mProjectTask = projectTaskService.addProjectTask(project_identifier, projectTask, principal.getName());
		
		return new ResponseEntity<ProjectTask>(mProjectTask, HttpStatus.CREATED);
	}
	
	@GetMapping("/{project_identifier}")
	public Iterable<ProjectTask> getProjectBacklog(@PathVariable String project_identifier, Principal principal) {
		return projectTaskService.findBacklogByProjectIdentifier(project_identifier, principal.getName());
	}
	
	@GetMapping("/{project_identifier}/{projectTask_seq}")
	public ResponseEntity<?> getProjectTask(@PathVariable String project_identifier, @PathVariable String projectTask_seq, Principal principal) {
		ProjectTask projectTask = projectTaskService.findProjectTaskByProjectSequence(project_identifier, projectTask_seq, principal.getName());
		return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
	}
	
	@PatchMapping("/{project_identifier}/{projectTask_seq}")
	public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
												@PathVariable String project_identifier, @PathVariable String projectTask_seq, Principal principal) {
		
		ResponseEntity<?> errorMap = mapValidationErrorService.getMapValidationService(result);
		if(errorMap != null) return errorMap;	
		
		ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask, project_identifier, projectTask_seq, principal.getName());
		
		return new ResponseEntity<ProjectTask>(updatedTask, HttpStatus.OK);
	}

	@DeleteMapping("/{project_identifier}/{projectTask_seq}")
	public ResponseEntity<?> deleteProjectTask(@PathVariable String project_identifier, @PathVariable String projectTask_seq, Principal principal) {
		projectTaskService.deleteProjectTaskByProjectSequence(project_identifier, projectTask_seq, principal.getName());
		
		return new ResponseEntity<String>("Project task " + projectTask_seq + " was deleted successfully", HttpStatus.OK);
	}
	
}
