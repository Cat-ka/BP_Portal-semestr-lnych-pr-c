package com.portal.controllerImpl;

import com.portal.constents.PortalConstants;
import com.portal.controller.SemesterFileController;
import com.portal.model.SemesterFile;
import com.portal.service.SemesterFileService;
import com.portal.utils.PortalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
public class SemesterFileControllerImpl implements SemesterFileController {

    @Autowired
    SemesterFileService semesterFileService;

    @Override
    public ResponseEntity<String> uploadFile(Integer projectId, MultipartFile file) {
        try {
            return semesterFileService.uploadFile(projectId, file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Integer id) {
        try {
            return semesterFileService.downloadFile(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<List<SemesterFile>> getFilesByProjectId(Integer projectId) {
        try {
            return semesterFileService.getFilesByProjectId(projectId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
