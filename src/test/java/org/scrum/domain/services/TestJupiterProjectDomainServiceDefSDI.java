package org.scrum.domain.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

//JUnit.5
@SpringBootTest
public class TestJupiterProjectDomainServiceDefSDI {
	private static final Logger logger = Logger.getLogger(TestJupiterProjectDomainServiceDefSDI.class.getName());

	@Autowired
	private IProjectDomainService service;

	@Autowired
	private IProjectEntityRepository repository;

	@Autowired // @Qualifier("projectFactory")
	private IProjectEntityFactory factory;

	@Test
	public void test() {
		logger.info("Repository implementation object:: " + repository);
		logger.info("Repository implementation class:: " + repository.getClass().getName());
		//
		logger.info("Factory implementation object:: " + factory);
		logger.info("Factory implementation class:: " + factory.getClass().getName());
		// CDI -> Service -> Factory @PostConstruct :: initDomainServiceEntities -- Repository
		logger.info("Service implementation object:: " + service);
		logger.info("Service implementation class:: " + service.getClass().getName());
		//
		Integer featureCount = service.getProjectFeaturesCount(1);
		assertTrue(featureCount > 0, "Features not counting...");
		logger.info("Feature count autowired xml:: " + featureCount);
	}

	@Autowired @Qualifier("ProjectEntityFactoryBaseCDI")
	private IProjectEntityFactory projectFactory;
	
	@Test
	public void testProjectEntityFactory() {
		// CDI -> AppContextConfig -> @Produces IProjectEntityFactory :: initDomainServiceEntities -- Repository
		int projectCounts = projectFactory.getProjectEntityRepository().size();
		assertTrue(projectCounts > 0, "Projects not counting...");
		logger.info("Project count:: " + projectCounts);
	}
}