package com.portal.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(path = "/file")
public interface SemesterFileController {

    @PostMapping(path = "/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam Integer semesterProjectId);

    @GetMapping(path = "/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer id);
}
