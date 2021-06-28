package com.crowdfunding.projectservice.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"creationDate"}, allowGetters = true)
@Document(collection = "project")
public class Project implements Serializable {

    @Id
    private String id;

    private String name;
    private String description;
    private String shortIdea;
    private Double fundGoal;
    private Date creationDate = new Date();
    private Date endDate;
    //private Double raisedFunds;
    private String category;
    private String imgUrl;

    //private Picture picture;
    private List<Announcement> announcements = new ArrayList<>();
    //private Integer idPublisher;

}
