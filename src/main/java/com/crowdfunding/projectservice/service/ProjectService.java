package com.crowdfunding.projectservice.service;

import com.crowdfunding.projectservice.exception.EntityNotFoundException;
import com.crowdfunding.projectservice.model.Announcement;
import com.crowdfunding.projectservice.model.Project;
import com.crowdfunding.projectservice.model.ProjectCreator;
import com.crowdfunding.projectservice.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(String id){
        if(id == null){
            log.error("Project id is null");
            return null;
        }
         Project project = projectRepository.findById(id);
         if(project==null){
             throw new EntityNotFoundException("Project not found with the following id"+ id);
         }
         return project;
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

    public List<Announcement> getAnnouncements(String projectId) {
        Project project = mongoTemplate.findOne
                (new Query(Criteria.where("id").is(projectId)), Project.class);

        if (project != null) {
            List<Announcement> announcements = project.getAnnouncements();
            return announcements;
        }
        return null;
    }


    public void deleteProject(String projectId) {
        // delete project
        projectRepository.delete(getProjectById(projectId));
        // delete the reference of the project in ProjectCreator collection
        Query query = Query.query(Criteria.where("$id").is(new ObjectId(projectId)));
        Update update = new Update().pull("project", query);
        mongoTemplate.updateMulti(new Query(), update, ProjectCreator.class);
    }

    public Project updateProject(Project project) {
        Project updatedProject = projectRepository.findById(project.getId());
        if(updatedProject==null){
            throw new EntityNotFoundException("Project not found with the following id"+ project.getId());
        }
        updatedProject.setName(project.getName());
        updatedProject.setDescription(project.getDescription());
        updatedProject.setShortIdea(project.getShortIdea());
        updatedProject.setFundGoal(project.getFundGoal());
        updatedProject.setEndDate(project.getEndDate());
        updatedProject.setCategory(project.getCategory());
        return projectRepository.save(updatedProject);
    }
}
