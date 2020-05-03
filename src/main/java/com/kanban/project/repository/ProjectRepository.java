package com.kanban.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kanban.project.domain.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
	
	Project findByProjectIdentifier(String projectId);

	Iterable<Project> findAllByProjectLeader(String username);
}
