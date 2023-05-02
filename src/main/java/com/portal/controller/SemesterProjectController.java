package com.portal.controller;

import com.portal.model.SemesterProject;
import com.portal.wrapper.SemesterProjectWrapper;
import jakarta.annotation.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/semester")
public interface SemesterProjectController {
    @PostMapping(path = "/add")
    public ResponseEntity<String> addSemesterProject(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/get")
    public ResponseEntity<List<SemesterProjectWrapper>> getAllSemesterProjects();

    @GetMapping(path = "/getPublished")
    public ResponseEntity<List<SemesterProjectWrapper>> getPublishedProjects();

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id);

    @PostMapping(path = "/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/assign")
    public ResponseEntity<String> assignProject(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/cancel")
    public ResponseEntity<String> cancelProject(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/currentUser")
    public ResponseEntity<String> currentUser();

    @GetMapping(path = "/project/{id}")
    public ResponseEntity<SemesterProjectWrapper> getPublishedProjectById(@PathVariable Integer id);

    @PostMapping(path = "/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("id") Integer semesterProjectId,
                                             @RequestParam("file") MultipartFile file);

    @GetMapping(path = "/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("id") Integer semesterProjectId);

    @GetMapping(path = "/getSemestersForTeachers/{userId}")
    public ResponseEntity<List<SemesterProjectWrapper>> getSemestersForTeacher(@PathVariable("userId") Integer userId);

    @GetMapping(path = "/getSemestersByCurrentUser")
    public ResponseEntity<List<SemesterProjectWrapper>> getSemestersByCurrentUser();


    @GetMapping(path = "/getSemesterProjectsForStudent")
    public ResponseEntity<List<SemesterProjectWrapper>> getSemesterProjectsForStudent();
}
