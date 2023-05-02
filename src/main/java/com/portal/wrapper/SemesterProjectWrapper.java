package com.portal.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemesterProjectWrapper {

    private Integer id;
    private String name;
    private String description;
    private Integer createdById;
    private String createdBy;
    private String term;
    private Boolean available;
    private String status;
    private Integer subjectId;
    private String subjectName;
    private Integer userId;
    private String userFirstName;
    private String userLastName;
    private String userFullName = userFirstName + " " + userLastName;
    private byte[] file;

    public SemesterProjectWrapper(Integer id, String name, String description, LocalDate term,
                                  String status, Integer createdById, String createdBy, Boolean available, Integer subjectId,
                                  String subjectName, Integer userId, String userFullName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.term = String.valueOf(term);
        this.status = status;
        this.createdById = createdById;
        this.createdBy = createdBy;
        this.available = available;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.userId = userId;
        this.userFullName = userFullName;
    }


    public SemesterProjectWrapper(Integer id, String name, String description, LocalDate term, Integer createdById,
                                  String createdBy, Boolean available, Integer subjectId,
                                  String subjectName, Integer userId, String userFirstName, String userLastName,
                                  String userFullName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdById = createdById;
        this.createdBy = createdBy;
        this.term = String.valueOf(term);
        this.available = available;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userFullName = userFullName;
    }

   /* @Autowired
    public SemesterProjectWrapper(Integer id, String name, String description, Integer createdById,
                                  String createdBy, LocalDate term, Integer subjectId,
                                  String subjectName, Integer userId, String userFirstName, String userLastName,
                                  String userFullName, byte[] file) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdById = createdById;
        this.createdBy = createdBy;
        this.term = String.valueOf(term);
        //this.available = available;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userFullName = userFullName;
        this.file = file;
    }*/
}

