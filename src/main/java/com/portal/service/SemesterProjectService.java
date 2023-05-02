package com.portal.service;

import com.portal.wrapper.SemesterProjectWrapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface SemesterProjectService {

    ResponseEntity<String> addSemesterProject(Map<String, String> requestMap);

    ResponseEntity<List<SemesterProjectWrapper>> getAllSemesterProjects();

    ResponseEntity<String> deleteSemesterProject(Integer id);

    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    ResponseEntity<List<SemesterProjectWrapper>> getPublishedProjects();

    ResponseEntity<String> assignProject(Map<String, String> requestMap);

    ResponseEntity<String> getCurrentUser();

    ResponseEntity<String> cancelProject(Map<String, String> requestMap);

    ResponseEntity<SemesterProjectWrapper> getPublishedProjectById(Integer id);

    ResponseEntity<String> uploadFile(Integer semesterProjectId, MultipartFile file);

    ResponseEntity<ByteArrayResource> downloadFile(Integer semesterProjectId);

    ResponseEntity<List<SemesterProjectWrapper>> getSemestersForTeacher(Integer id);

    ResponseEntity<List<SemesterProjectWrapper>> getSemestersByCurrentUser();

    ResponseEntity<List<SemesterProjectWrapper>> getSemesterProjectsForStudent();
}
