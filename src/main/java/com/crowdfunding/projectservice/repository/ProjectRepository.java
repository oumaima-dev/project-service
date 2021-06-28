package com.crowdfunding.projectservice.repository;

import com.crowdfunding.projectservice.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, Integer> {

    Project findById(String id);
}
