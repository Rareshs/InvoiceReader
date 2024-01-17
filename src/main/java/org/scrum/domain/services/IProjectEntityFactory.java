package org.scrum.domain.services;

import org.scrum.domain.project.Project;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IProjectEntityFactory {
	
	// build project with: 1 release, startDate is Now, release publish date: startDate + 1 Month
	public Project buildSimpleProject(String projectName);
	// build project with: 2 releases
	public Project buildProjectWith2R(String projectName, Date startDate, Integer releaseIntervalInMonths);
	// build project with: 2 releases
	public Project buildProjectWith2R(String projectName, LocalDateTime startDate, Integer releaseIntervalInMonths);
	// build project with: 2 releases
	public Project buildProjectWithXR(String projectName, List<Date> releaseStartDates);
	// Dependencies to get IDs
	public void setProjectEntityRepository(IProjectEntityRepository repository);
	public IProjectEntityRepository getProjectEntityRepository();
	
}

// build entity from DTO
// public Project toEntity(Project projectDTO);
