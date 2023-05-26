package com.portal.service;

import com.portal.model.SemesterFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SemesterFileService {

    //ResponseEntity<String> uploadFile(Integer semesterProjectId, byte[] fileData);

    //ResponseEntity<byte[]> downloadFile(Integer id);

    ResponseEntity<String> uploadFile(Integer projectId, MultipartFile file);

    ResponseEntity<Resource> downloadFile(Integer id);


    ResponseEntity<List<SemesterFile>> getFilesByProjectId(Integer projectId);
}