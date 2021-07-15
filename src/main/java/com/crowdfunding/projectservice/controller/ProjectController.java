package com.crowdfunding.projectservice.controller;

import com.crowdfunding.projectservice.model.Announcement;
import com.crowdfunding.projectservice.model.Project;
import com.crowdfunding.projectservice.model.ProjectCreator;
import com.crowdfunding.projectservice.service.CloudinaryService;
import com.crowdfunding.projectservice.service.ProjectCreatorService;
import com.crowdfunding.projectservice.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ProjectCreatorService projectCreatorService;

    @GetMapping("/projectCreator/{userId}")
    public ProjectCreator getCreatorById(@PathVariable String userId){
        return projectCreatorService.getCreatorByUserId(userId);

    }


    @GetMapping("/all")
    public List<Project> getAllProjects(){
        return projectService.getProjects();
    }

    @GetMapping("/{projectId}")
    public Project getProjectById(@PathVariable String projectId){
        return projectService.getProjectById(projectId);

    }


    @PostMapping("/add")
    public Project addProject(@RequestParam("project") String project, @RequestParam("userId") String userId,  @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile) throws IOException {
       // project.setCreationDate(Instant.now());
        ObjectMapper mapper = new ObjectMapper();
        Project p = mapper.readValue(project, Project.class);
        Map result = cloudinaryService.upload(multipartFile);
        String url = (String) result.get("url");
        p.setImgUrl(url);
        Project savedProject = projectService.saveProject(p);
        if(savedProject != null){
            projectCreatorService.addProjectCreator(savedProject,userId);
        }
        return savedProject;
    }

    @PostMapping("/addInvestment/{funds}")
    public ResponseEntity<?> addInvestment(@RequestParam("projectId") String projectId, @RequestParam("userId") String userId, @PathVariable Long funds){
        Project project =  projectService.getProjectById(projectId);
        projectCreatorService.addInvestments(project,userId,funds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId){
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Project> updateProject(@RequestBody Project project){
        return new ResponseEntity<>(projectService.updateProject(project), HttpStatus.OK);
    }

/*    @PostMapping("/addProjectCreator")
    public ResponseEntity<?> addProjectCr(){
        ProjectCreator pc = new ProjectCreator();
        return new ResponseEntity(projectCreatorService.save(pc), HttpStatus.OK);
    }*/

    // return user's projects
    @GetMapping("/getMyProjects/{id}")
    public List<Project> getMyProjects(@PathVariable String id){
        return projectCreatorService.getMyProjects(id);
    }

    @GetMapping("/getMyProjects")
    public Optional<ProjectCreator> getMyProjectsbyid(@PathVariable String id){
        return projectCreatorService.getCreatorById(id);
    }

    @PostMapping("/{projectId}/newAnnouncement")
    public ResponseEntity<?> addAnnouncement
            (@PathVariable String projectId, @RequestBody Announcement announcement){

        return new ResponseEntity(projectService.saveAnnouncement(announcement,projectId), HttpStatus.OK);
    }

    @GetMapping("/{projectId}/getAnnouncement")
    public ResponseEntity<?> getAnnouncement
            (@PathVariable String projectId){

        return new ResponseEntity(projectService.getAnnouncements(projectId), HttpStatus.OK);
    }


}
