package com.portal.controllerImpl;

import com.portal.constents.PortalConstants;
import com.portal.controller.SemesterFileController;
import com.portal.service.SemesterFileService;
import com.portal.utils.PortalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
public class SemesterFileControllerImpl implements SemesterFileController {

    @Autowired
    SemesterFileService semesterFileService;

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file, Integer semesterProjectId) {
        try {
            return semesterFileService.uploadFile(semesterProjectId, file.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PortalUtils.getResponseEntity(PortalConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadFile(Integer id) {
        try {
            return semesterFileService.downloadFile(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
