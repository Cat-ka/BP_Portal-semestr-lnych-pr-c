package com.portal.controllerImpl;

import com.portal.constents.PortalConstants;
import com.portal.controller.SemesterProjectController;
import com.portal.service.SemesterProjectService;
import com.portal.utils.PortalUtils;
import com.portal.wrapper.SemesterProjectWrapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class SemesterProjectControllerImpl implements SemesterProjectController {

    @Autowired
    SemesterProjectService semesterProjectService;

    @Override
    public ResponseEntity<String> addSemesterProject(Map<String, String> requestMap) {
        try {
            return semesterProjectService.addSemesterProject(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<SemesterProjectWrapper>> getAllSemesterProjects() {
        try {
            return semesterProjectService.getAllSemesterProjects();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<SemesterProjectWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<SemesterProjectWrapper>> getPublishedProjects() {
        try {
            return semesterProjectService.getPublishedProjects();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<SemesterProjectWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> delete(Integer id) {
        try {
            return semesterProjectService.deleteSemesterProject(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            return semesterProjectService.updateStatus(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> assignProject(Map<String, String> requestMap) {
        try {
            return semesterProjectService.assignProject(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> cancelProject(Map<String, String> requestMap) {
        try {
            return semesterProjectService.cancelProject(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> currentUser() {
        try {
            return semesterProjectService.getCurrentUser();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


     @Override
     public ResponseEntity<String> uploadFile(Integer semesterProjectId, MultipartFile file) {
         try {
             return semesterProjectService.uploadFile(semesterProjectId, file);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
     }

    @Override
    public ResponseEntity<ByteArrayResource> downloadFile(Integer semesterProjectId) {
        try {
            return semesterProjectService.downloadFile(semesterProjectId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<List<SemesterProjectWrapper>> getSemestersForTeacher(Integer id) {
        try {
            return semesterProjectService.getSemestersForTeacher(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<SemesterProjectWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<SemesterProjectWrapper>> getSemestersByCurrentUser() {
        try {
            return semesterProjectService.getSemestersByCurrentUser();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<SemesterProjectWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<SemesterProjectWrapper>> getSemesterProjectsForStudent() {
        try {
            return semesterProjectService.getSemesterProjectsForStudent();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<SemesterProjectWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<SemesterProjectWrapper> getPublishedProjectById(Integer id) {
        try {
            return semesterProjectService.getPublishedProjectById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<SemesterProjectWrapper>(new SemesterProjectWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
