package com.crowdfunding.projectservice.controller;

import com.crowdfunding.projectservice.model.Announcement;
import com.crowdfunding.projectservice.model.Project;
import com.crowdfunding.projectservice.service.CloudinaryService;
import com.crowdfunding.projectservice.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    @Autowired
    private CloudinaryService cloudinaryService;


    @GetMapping("/all")
    public List<Project> getAllProjects(){
        return projectService.getProjects();
    }



    @PostMapping("/add")
    public Project addProject(@RequestParam("project") String project, @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile) throws IOException {
       // project.setCreationDate(Instant.now());
        ObjectMapper mapper = new ObjectMapper();
        Project p = mapper.readValue(project, Project.class);
        Map result = cloudinaryService.upload(multipartFile);
        String url = (String) result.get("url");
        p.setImgUrl(url);
        return projectService.saveProject(p);
    }

    @PostMapping("/{projectId}/newAnnouncement")
    public ResponseEntity<?> addAnnouncement
            (@PathVariable String projectId, @RequestBody Announcement announcement){

        return new ResponseEntity(projectService.saveAnnouncement(announcement,projectId), HttpStatus.OK);
    }

}
