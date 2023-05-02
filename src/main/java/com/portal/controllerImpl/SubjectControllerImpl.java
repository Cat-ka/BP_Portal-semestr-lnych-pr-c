package com.portal.controllerImpl;

import com.portal.constents.PortalConstants;
import com.portal.controller.SubjectController;
import com.portal.model.Subject;
import com.portal.service.SubjectService;
import com.portal.utils.PortalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SubjectControllerImpl implements SubjectController {

    @Autowired
    SubjectService subjectService;

    @Override
    public ResponseEntity<String> addSubject(Map<String, String> requestMap) {
        try {
            return subjectService.addSubject(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateSubject(Map<String, String> requestMap) {
        try {
            return subjectService.updateSubject(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteSubject(Integer id) {
        try {
            return subjectService.deleteSubject(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Map<String, Object>>> getSubjects(String filterValue) {
        try {
            return subjectService.getSubjects(filterValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getSubjectById(Integer id) {
        try {
            return subjectService.getSubjectById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

