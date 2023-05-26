package com.portal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.File;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "semester")
public class SemesterProject implements Serializable {

    public static final Long serialVersionUid = 123456L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "createdById")
    private Integer createdById;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "available")
    private Boolean available;
    //či je voľná alebo si ju už vybral niektorý študent

    @Column(name = "status")
    private String status;
    //zverejňená alebo nie

    @Column(name = "term", nullable = false)
    private LocalDate term;
    //dátum, ktorý zabezpečí, že po tomto termíne sa status zmení na false, alebo available na 0
    //alebo môžem použiť java.time - je tam viac funkcionalít a je flexibilnejší

    @ManyToOne(fetch = FetchType.LAZY)
    //toto znamená že subject bude načítaný až vtedy keď ju budeme chcieť načítať
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "fileData", columnDefinition = "LONGBLOB")
    private Blob fileData;

    @Column(name = "fileName")
    private String fileName;

    @JsonManagedReference
    @OneToMany(mappedBy = "semesterProject")
    private List<SemesterFile> files;


    public SemesterProject(Integer id, Blob fileData, String fileName) {
        this.id = id;
        this.fileData = fileData;
        this.fileName = fileName;
    }

    public SemesterProject(String name, String description, String createdBy, String status, LocalDate term, Subject subject) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.status = status;
        this.term = term;
        this.subject = subject;
    }

    public SemesterProject(Integer id, String name, String description, String createdBy, Boolean available,
                           String status, LocalDate term, Subject subjectId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.available = available;
        this.status = status;
        this.term = term;
        this.subject = subjectId;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Blob getFileData() {
        return fileData;
    }

    public void setFileData(Blob fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
