package com.portal.service;

import com.portal.model.Subject;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SubjectService {

    ResponseEntity<String> addSubject(Map<String,String> requestMap);

    ResponseEntity<String> deleteSubject(Integer id);

    ResponseEntity<String> updateSubject(Map<String, String> requestMap);

    ResponseEntity<List<Map<String, Object>>> getSubjects(String filterValue);

    ResponseEntity<Map<String, Object>> getSubjectById(Integer id);


}
