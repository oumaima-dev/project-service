package com.crowdfunding.projectservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "projectCreator")
public class ProjectCreator implements Serializable {

    @Id
    private String id;
    private String userId;
    private Long amount = 0L;

    @Field("project")
    @DBRef
    private List<Project> myProjects = new ArrayList<>();

    @DBRef
    private List<Project> myInvestments = new ArrayList<>();
}