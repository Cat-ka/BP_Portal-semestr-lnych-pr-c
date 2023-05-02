package com.portal.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

public interface SemesterFileService {

    ResponseEntity<String> uploadFile(Integer semesterProjectId, byte[] fileData);

    ResponseEntity<ByteArrayResource> downloadFile(Integer id);
}