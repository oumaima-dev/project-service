package com.crowdfunding.projectservice.service;

import com.crowdfunding.projectservice.model.Announcement;
import com.crowdfunding.projectservice.model.Project;
import com.crowdfunding.projectservice.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }


    public Project saveProject(Project project) {

        Announcement a = new Announcement();
        project.getAnnouncements().add(a);
        return projectRepository.insert(project);
    }

    public Project saveAnnouncement(Announcement announcement, String projectId){
        Project project = mongoTemplate.findOne
                (new Query(Criteria.where("id").is(projectId)), Project.class);

        if (project != null) {
            project.getAnnouncements().add(announcement);
            mongoTemplate.save(project);
            return project;
        }
        return null;
    }
}
