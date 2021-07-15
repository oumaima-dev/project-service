package com.crowdfunding.projectservice.repository;

import com.crowdfunding.projectservice.model.ProjectCreator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectCreatorRepository extends MongoRepository<ProjectCreator, String> {
    ProjectCreator findByUserId(String userId);
}
