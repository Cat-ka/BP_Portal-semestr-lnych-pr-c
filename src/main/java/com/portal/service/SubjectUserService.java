package com.portal.service;

import com.portal.model.Subject;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SubjectUserService {

    ResponseEntity<String> deleteSubjectUsersBySubjectId(Integer subjectId);

    ResponseEntity<String> updateSubjectUsers(Map<String, String> requestMap);

    ResponseEntity<List<String>> getSubjectByUserId(Integer userId);

}
