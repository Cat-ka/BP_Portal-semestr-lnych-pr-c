package com.portal.controllerImpl;

import com.portal.controller.SubjectUserController;
import com.portal.model.Subject;
import com.portal.service.SubjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


public class SubjectUserControllerImpl implements SubjectUserController {

    @Autowired
    SubjectUserService subjectUserService;

    @Override
    public ResponseEntity<List<String>> findByUserId(Integer userId) {
        try {
            return subjectUserService.getSubjectByUserId(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
