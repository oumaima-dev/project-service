package com.crowdfunding.projectservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    @Id
    private String id;
    private String message;
    private Date AnnCreationDate;
}
