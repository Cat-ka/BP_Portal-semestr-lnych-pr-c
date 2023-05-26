package com.portal.controller;

import com.portal.model.SemesterFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/file")
public interface SemesterFileController {

    @PostMapping(path = "/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("semester_project_id") Integer projectId,
                                             @RequestParam("file") MultipartFile file);

    @GetMapping(path = "/download/{semester_project_id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("semester_project_id") Integer id);

    @GetMapping(path = "/get/{projectId}")
    ResponseEntity<List<SemesterFile>> getFilesByProjectId(@PathVariable Integer projectId);
}
