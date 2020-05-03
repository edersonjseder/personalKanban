package com.kanban.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kanban.project.domain.Backlog;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog, Long> {
	
	Backlog findByProjectIdentifier(String identifier);

}
