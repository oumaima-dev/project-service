package com.crowdfunding.projectservice.service;

import com.crowdfunding.projectservice.exception.EntityNotFoundException;
import com.crowdfunding.projectservice.model.Project;
import com.crowdfunding.projectservice.model.ProjectCreator;
import com.crowdfunding.projectservice.repository.ProjectCreatorRepository;
import com.mongodb.client.DistinctIterable;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectCreatorService {

    @Autowired
    private ProjectCreatorRepository projectCreatorRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public ProjectCreator save(ProjectCreator pc) {
        return projectCreatorRepository.save(pc);
    }

    // return user's projects
    public List<Project> getMyProjects(String userId) {
        ProjectCreator pc = getCreatorByUserId(userId);
        return pc.getMyProjects();
    }

    // return user's investments
    public List<Project> getMyInvestments(String userId) {
        List<Project> projects= mongoTemplate.query(ProjectCreator.class).distinct("myInvestments").as(Project.class).all();

        return projects;
    }

    public Optional<ProjectCreator> getCreatorById(String id) {
        if(id == null){
            log.error("Creator id is null");
            return null;
        }
        Optional<ProjectCreator> creator = projectCreatorRepository.findById(id);
        if(creator==null){
            throw new EntityNotFoundException("Creator not found with the following id"+ id);
        }
        return creator;
    }

    public ProjectCreator getCreatorByUserId(String userId) {
        if(userId == null){
            log.error("Creator id is null");
            return null;
        }
        ProjectCreator creator = projectCreatorRepository.findByUserId(userId);
        if(creator==null){
            return null;
        }else{
            return creator;
        }
    }

    public void addProjectCreator(Project project, String userId){
        ProjectCreator pc = getCreatorByUserId(userId);
        if(pc == null){
            ProjectCreator newCreator = new ProjectCreator();
            newCreator.setUserId(userId);
            newCreator.getMyProjects().add(project);
            save(newCreator);
        }
        else{
            pc.getMyProjects().add(project);
            save(pc);
        }
    }

    public void addInvestments(Project project,String userId, Long funds){
        ProjectCreator pc = getCreatorByUserId(userId);
        if(pc == null){
            ProjectCreator newCreator = new ProjectCreator();
            newCreator.setUserId(userId);
            newCreator.getMyInvestments().add(project);
            newCreator.setAmount(funds);
            save(newCreator);
        }
        else{
            pc.getMyInvestments().add(project);
            Long amount = pc.getAmount() + funds;
            pc.setAmount(amount);
            save(pc);
        }
    }
}
